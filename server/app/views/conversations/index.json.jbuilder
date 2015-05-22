json.array! @conversations do |conversation|
  json.extract! conversation, :id
  json.last_message do
    last_message = conversation.messages.order(created_at: :desc).first
    json.extract! last_message, :id, :title, :body
    json.own last_message.sender == current_user
  end

  json.sender do
    json.partial! 'shared/user_box', user: conversation.sender
  end

  json.recipient do
    json.partial! 'shared/user_box', user: conversation.recipient
  end
end
