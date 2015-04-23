require 'spec_helper'

describe UsersController, type: :controller do
  let(:current_user) { create(:user, phone: "+79165730095") }
  let(:user) { create(:user, phone: "+79152004797") }
  before { allow(subject).to receive(:current_user).and_return(current_user) }

  context '#index' do
  end

  context '#update' do
    context 'add contact' do
      before { put :update, id: current_user.id, phones: ['+79152004797'] }

      it { expect(current_user.contacts.count).to eq 1 }
    end
  end
end
