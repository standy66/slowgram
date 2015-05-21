class UsersController < ApplicationController
  before_action :signed_in_user

  def index
    render json: { contacts: current_user.contacts }, status: 200
  end

  def update
    new_phones = JSON.parse(params['phones'])
    new_phones.each do |phone|
      current_user.contact_relations.create(contact: User.find_by_phone(phone))
    end
    render json: { contact: current_user.contacts }, status: 200
  end

end
