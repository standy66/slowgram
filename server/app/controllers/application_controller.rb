class ApplicationController < ActionController::Base
  include ApplicationHelper
  include SessionsHelper


  def signed_in?
    current_user.present?
  end

  private

    def signed_in_user
      head 401 unless signed_in?
    end
end
