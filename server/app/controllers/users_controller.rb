class UsersController < ApplicationController
  before_action :signed_in_user

  def index
    render json: { contacts: current_user.contacts }, status: 200
  end

  private

    def signed_in_user
      render json: {}, status: 401 unless signed_in?
    end
end
