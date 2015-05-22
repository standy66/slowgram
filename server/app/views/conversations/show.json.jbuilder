json.messages @messages do |message|
  json.extract! message, :id, :title, :body, :delivered_at
  json.own message.sender == current_user
end

json.sender do
  json.partial! 'shared/user_box', user: @conversation.sender
end

json.recipient do
  json.partial! 'shared/user_box', user: @conversation.recipient
end
