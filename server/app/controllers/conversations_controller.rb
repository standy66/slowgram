class ConversationsController < ApplicationController
  before_action :signed_in_user

  def index
    @conversations = current_user.conversations
  end

  def show
    @conversation = current_user.conversations.find(params['id']) if params['id'].present?
    @conversation ||= Conversations.own_for(sender, recipient).first
    @messages = @conversation.messages
  end

  def create
    if sender.id != current_user.id
      return render json: {}, status: :unprocessable_entity
    end

    users_to_conversation = UsersToConversation.new(sender: sender, recipient: recipient)

    if users_to_conversation.save
      render json: users_to_conversation.attributes, status: :created
    else
      render json: users_to_conversation.errors, status: :unprocessable_entity
    end
  end
end
