class Message < ActiveRecord::Base
  belongs_to :conversation
  has_one :sender, through: :conversation
  has_one :recipient, through: :conversation

  validates_presence_of :title, :body, :conversation
end
