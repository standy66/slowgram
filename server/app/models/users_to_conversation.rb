class UsersToConversation < ActiveRecord::Base
  belongs_to :sender, class_name: 'User'
  belongs_to :recipient, class_name: 'User'
  belongs_to :conversation

  validates :sender, uniqueness: { scope: :recipient }

  before_create :create_conversation

  after_create :duplicate

  private

  def duplicate
    UsersToConversation.find_or_create_by(sender: self.recipient, recipient: self.sender,
                               conversation: self.conversation)
  end

  def create_conversation
    if self.conversation.blank?
      self.conversation = Conversation.create
    end
  end
end
