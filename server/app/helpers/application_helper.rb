module ApplicationHelper
  def current_user
    remember_token = User.encrypt(params['remember_token'])
    @current_user ||= User.find_by(remember_token: remember_token)
  end

  def sender
    @sender ||= User.where(id: params['sender_id']).first if params['sender_id'].present?
    @sender ||= current_user
  end

  def recipient
    @recipient ||= User.where(id: params['recipient_id']).first if params['recipient_id'].present?
    @recipient ||= User.find_by_phone(params['recipient_phone']) if params['recipient_phone'].present?
  end
end
