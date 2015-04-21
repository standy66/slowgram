class User < ActiveRecord::Base
  phony_normalize :phone

  validates :phone, presence: true
  validates :phone, phony_plausible: true

  has_many :contacts, class_name: :User, foreign_key: :id

  before_create :create_remember_token

  def User.new_remember_token
    SecureRandom.base64.tr("+/", "-_")
  end

  def User.encrypt(token)
    Digest::SHA1.hexdigest(token.to_s)
  end

  private

    def create_remember_token
      self.remember_token = User.encrypt(User.new_remember_token)
    end
end
