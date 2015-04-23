require 'spec_helper'

describe SessionsController, type: :controller do
  context 'login' do

    context 'create' do
      before { post :create, phone: '79165730095' }

      it { expect(response.status).to eq 201 }
      it { expect(JSON.parse(response.body)).to have_key('remember_token') }
    end

    context 'bad authenticate' do
      before { post :create, phone: '79165730095' }
      let(:remember_token) { JSON.parse(response.body)['remember_token'] }
    end
  end
end
