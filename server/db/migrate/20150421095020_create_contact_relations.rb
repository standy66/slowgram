class CreateContactRelations < ActiveRecord::Migration
  def change
    create_table :contact_relations do |t|

      t.timestamps null: false
    end
  end
end
