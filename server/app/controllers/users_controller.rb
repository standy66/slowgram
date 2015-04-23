class UsersController < ApplicationController
  before_action :signed_in_user

  def index
    render json: { contacts: current_user.contacts }, status: 200
  end

  def update
    new_phones = JSON.parse(params['phones'])
    new_phones.each do |phone|
      phone = PhonyRails.normalize_number(phone)
      current_user.contact_relations.create(contact: User.where(phone: phone).first)
    end
    render json: { contact: current_user.contacts }, status: 200
  end

  private

    def signed_in_user
      head 401 unless signed_in?
    end
end
