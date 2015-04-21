module SessionsHelper
  def sign_in(user)
    @remember_token = User.new_remember_token
    user.update_attribute(:remember_token, User.encrypt(@remember_token))
    @current_user = user
  end

  def sign_out
    if current_user.present?
      current_user.update_attribute(:remember_token, User.encrypt(User.new_remember_token))
      @current_user = nil
    end
  end
end
