class ConversationsController < ApplicationController
  before_action :signed_in_user

  def index
    @conversations = Conversation.all_for(sender)
  end

  def show
    @conversation = Conversation.find(params['id']) if params['id'].present?
    @conversation ||= Conversation.own_for(sender, recipient).first

    if @conversation.blank? || ![@conversation.sender, @conversation.recipient].include?(current_user)
      render json: {}, status: :unprocessable_entity
    end

    @messages = @conversation.messages
    @messages = @messages.where('delivered_at < ?', Time.now)
  end

  def create
    if sender.id != current_user.id || recipient.blank?
      return render json: {}, status: :unprocessable_entity
    end

    conversation = Conversation.new(sender: sender, recipient: recipient)

    if conversation.save
      render json: conversation.attributes, status: :created
    else
      render json: conversation.errors, status: :unprocessable_entity
    end
  end
end
