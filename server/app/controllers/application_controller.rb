class ApplicationController < ActionController::Base
  include SessionsHelper

  def current_user
    remember_token = User.encrypt(params['remember_token'])
    @current_user ||= User.find_by(remember_token: remember_token)
    Rails.logger.debug User.all.inspect
    @current_user
  end

  def signed_in?
    current_user.present?
  end
end
