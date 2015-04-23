require 'spec_helper'

describe "Authentication spec", type: :request do
   context 'authenticate' do
      before { post '/login', phone: '79165730095' }
      let(:remember_token) { JSON.parse(response.body)['remember_token'] }
      before { get users_path, remember_token: remember_token }

      it { expect(response.status).to eq 200 }
    end
end
