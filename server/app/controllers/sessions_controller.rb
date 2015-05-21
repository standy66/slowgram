class SessionsController < ApplicationController
  def new
  end

  def create
    user = User.find_or_create_by(phone: PhonyRails.normalize_number(params['phone']))
    if user.valid?
      sign_in user
      render json: { remember_token: @remember_token }, status: 201
    else
      render json: user.errors.full_messages, status: 403
    end
  end

  def destroy
    sign_out
    head 200
  end
end
