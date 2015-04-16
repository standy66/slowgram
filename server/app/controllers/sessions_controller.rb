class SessionsController < ApplicationController
  def new
  end

  def create
    user = User.find_or_create_by(phone: PhonyRails.normalize_number(params['phone']))
    sign_in user

    render json: { remember_token: @remember_token }, status: 201
  end

  def destroy
    sign_out
  end
end
