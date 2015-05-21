class Conversation < ActiveRecord::Base
  has_one :users_to_conversation
  has_one :sender, through: :users_to_conversation
  has_one :recipient, through: :users_to_conversation

  has_many :messages, dependent: :destroy

  scope :own_for, ->(user) {
    joins(:users_to_conversation).
    where(users_to_conversations: { sender_id: user.id })
  }

  scope :own_for, ->(sender, recipient) {
    joins(:users_to_conversation).
    where(users_to_conversations: { sender_id: sender.id, recipient_id: recipient.id })
  }

  scope :ordered_by_messages, -> {
    joins(<<-SQL
      join (
        select conversation_id, max(created_at) as max_created
        from messages
        group by conversation_id
      ) recent on recent.conversation_id = conversations.id
    SQL
    ).order('max_created desc')
  }
end
