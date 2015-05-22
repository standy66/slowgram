class Conversation < ActiveRecord::Base
  belongs_to :sender, class_name: 'User'
  belongs_to :recipient, class_name: 'User'

  has_many :messages, dependent: :destroy

  validates_presence_of :sender, :recipient
  validate :users_uniqueness
  validate :check_sender_and_recipient

  scope :all_for, ->(user) {
    where('conversations.sender_id = :user_id or conversations.recipient_id = :user_id', user_id: user.id)
  }

  scope :own_for, ->(sender, recipient) {
    where('(conversations.sender_id = :sender_id and conversations.recipient_id = :recipient_id) or
           (conversations.sender_id = :recipient_id and conversations.recipient_id = :sender_id)',
           sender_id: sender.id, recipient_id: recipient.id)
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


  def check_sender_and_recipient
    errors.add(:recipient, "can't be the same as sender") if sender == recipient
  end

  def users_uniqueness
    errors.add(:errors, "there are the same conversation") if Conversation.own_for(sender, recipient).count > 0
  end
end
