class MessagesController < ApplicationController
  before_action :signed_in_user

  def create
    if sender.id != current_user.id
      return render json: {}, status: :unprocessable_entity
    end

    @message = conversation.messages.new(message_params)

    if @message.save
      render json: @message.attributes, status: :created
    else
      render json: @message.errors, status: :unprocessable_entity
    end
  end

  def message_params
    params.permit(:title, :body)
  end

  def conversation
    @conversation ||= get_conversation
  end

  def get_conversation
    conversation = Conversation.where(id: params['conversation_id']).first
    conversation ||= Conversation.own_for(sender, recipient).first

    return if wrong_params?(conversation)

    if conversation.blank?
      users_to_conversation = UsersToConversation.new(sender: sender, recipient: recipient)
      if users_to_conversation.save
        conversation = users_to_conversation.conversation
      else
        return render json: users_to_conversation.errors, status: :unprocessable_entity
      end
    end

    conversation
  end

  def wrong_params?(conversation)
    if conversation.present?
      return ([sender.id, recipient.id] - [conversation.sender.id, conversation.recipient.id]).present?
    else
      return false
    end
  end
end
