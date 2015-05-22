class UsersController < ApplicationController
  before_action :signed_in_user

  def index
    render json: { contacts: current_user.contacts }, status: 200
  end

  def update
    if params['new_phones'].present?
      new_phones = JSON.parse(params['new_phones'])
      new_phones.each do |phone|
        current_user.contact_relations.create(contact: User.find_by_phone(phone))
      end
    end

    current_user.update_attributes(user_params)

    render json: current_user.attributes, status: 200
  end

  def user_params
    params.permit(:name, :avatar)
  end
end
