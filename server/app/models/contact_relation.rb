class ContactRelation < ActiveRecord::Base
  belongs_to :user
  belongs_to :contact, class_name: 'User'

  validates_presence_of :user
  validates_presence_of :contact
end
