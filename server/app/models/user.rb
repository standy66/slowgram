class User < ActiveRecord::Base
  phony_normalize :phone

  validates :phone, presence: true
  validates :phone, phony_plausible: true

  has_many :contact_relations
  has_many :contacts, through: :contact_relations

  has_many :users_to_conversations, foreign_key: 'sender_id'
  has_many :conversations, through: :users_to_conversations

  before_create :create_remember_token

  scope :find_by_phone, ->(phone) { where(phone: PhonyRails.normalize_number(phone)).first }

  def User.new_remember_token
    SecureRandom.base64.tr('+/', '-_')
  end

  def User.encrypt(token)
    Digest::SHA1.hexdigest(token.to_s)
  end

  private

    def create_remember_token
      self.remember_token = User.encrypt(User.new_remember_token)
    end
end
