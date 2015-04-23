class AddColumnsToContactRelation < ActiveRecord::Migration
  def change
    add_column :contact_relations, :user_id, :integer
    add_column :contact_relations, :contact_id, :integer
  end
end
