class CreateUsersToConversations < ActiveRecord::Migration
  def change
    create_table :users_to_conversations do |t|
      t.integer :sender_id
      t.integer :recipient_id
      t.integer :conversation_id

      t.timestamps null: false
    end
  end
end
