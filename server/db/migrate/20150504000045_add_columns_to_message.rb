class AddColumnsToMessage < ActiveRecord::Migration
  def change
    add_column :messages, :conversation_id, :integer
    add_column :messages, :title, :string
    add_column :messages, :body, :string
  end
end
