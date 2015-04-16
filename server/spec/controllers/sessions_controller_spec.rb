require 'spec_helper'

describe SessionsController, type: :request do
  context 'login' do
    before { post sessions_path, phone: '79165730095' }

    context 'create' do
      it { expect(response.status).to eq 201 }
      it { expect(JSON.parse(response.body)).to have_key('remember_token') }
    end

    context 'authenticate' do
      let(:remember_token) { JSON.parse(response.body)['remember_token'] }

      before { get users_path, remember_token: remember_token }

      it { expect(response.status).to eq 200 }
    end
  end
end
