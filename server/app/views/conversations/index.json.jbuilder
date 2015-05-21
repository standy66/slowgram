json.array! @conversations do |conversation|
  json.extract! conversation, :id
  json.message conversation.messages.order(created_at: :desc).first

  json.sender do
    json.partial! 'shared/user_box', user: conversation.sender
  end

  json.recipient do
    json.partial! 'shared/user_box', user: conversation.recipient
  end
end
