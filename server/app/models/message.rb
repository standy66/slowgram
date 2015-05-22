class Message < ActiveRecord::Base
  belongs_to :conversation
  belongs_to :sender, class_name: 'User'
  belongs_to :recipient, class_name: 'User'

  validates_presence_of :title, :body, :conversation, :sender, :recipient
  validate :check_sender_and_recipient
  validate :check_conversation_sender_recipient

  def check_sender_and_recipient
    errors.add(:recipient, "can't be the same as sender") if sender == recipient
  end

  def check_conversation_sender_recipient
    ([sender_id, recipient_id] - [conversation.sender.id, conversation.recipient.id]).blank?
  end
end
