package presenter;

import model.Message;
import model.PublicMessage;
import model.User;
import repository.ActionRepositoryImpl;
import repository.DataRepositoryImpl;
import tool.AsyncCallback;
import tool.StaticRepository;
import tool.exception.InputCanceledException;
import usecase.*;

import java.util.ArrayList;

public class MainPresenter extends Presenter<MainPresenter.View> {
    FriendListGet friendListGet;
    FriendListAdd friendListAdd;

    MessageListGet messageListGet;
    MessageSend messageSend;
    
    MessagePublicSend messagePublicSend;
    MessagePublicListGet messagePublicListGet;

    public MainPresenter() {
        this.friendListGet = new FriendListGet(new DataRepositoryImpl());
        this.friendListAdd = new FriendListAdd(new ActionRepositoryImpl());

        this.messageSend = new MessageSend(new ActionRepositoryImpl());
        this.messageListGet = new MessageListGet(new DataRepositoryImpl());
        
        this.messagePublicSend = new MessagePublicSend(new ActionRepositoryImpl());
        this.messagePublicListGet = new MessagePublicListGet(new DataRepositoryImpl());
        
    }

    public void menuItemEnter(int i) {
        try {
            switch (i){
                case 1: getView().renderNewFriend(); break;
                case 2: getView().renderFriendList(); break;
                
                case 3: getView().renderNewMessage(); break;
                case 4: getView().renderMessageList(); break;

                case 5: getView().renderNewPublicMessage(); break;
                case 6: getView().renderPublicMessageList(); break;
                
                case 0: return;
            }
        } catch (InputCanceledException e){
            getView().inputCanceled();
            getView().render();
        }
    }

    public void newFriend(String login) {
        friendListAdd.execute(new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess() {
                getView().showInfoMessage("Success!");
                getView().render();
            }

            @Override
            public void onFailure(Throwable caught) {
                getView().showInfoMessage("Error!");
                getView().render();
            }
        }, new FriendListAdd.Params(StaticRepository.SESSION_TOKEN, login));
    }

    public void getFriendList() {
        friendListGet.execute(new AsyncCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                getView().showFriendList(users);
                getView().render();
            }
        }, StaticRepository.SESSION_TOKEN);
    }

    public void newMessage(String login, String text) {
        messageSend.execute(new AsyncCallback<Void>() {
            @Override
            public void onSuccess() {
                getView().showInfoMessage("Success!");
                getView().render();
            }

            @Override
            public void onFailure(Throwable caught) {
                getView().showInfoMessage("Error!");
                getView().render();
            }
        }, new MessageSend.Params(StaticRepository.SESSION_TOKEN, login, text));
    }

    public void getMessageList(String login) {
        messageListGet.execute(new AsyncCallback<ArrayList<Message>>() {
            @Override
            public void onSuccess(ArrayList<Message> messages) {
                getView().showMessageList(messages);
                getView().render();
            }

            @Override
            public void onFailure(Throwable caught) {
                getView().showInfoMessage("No Message Found!");
                getView().render();
            }
        }, new MessageListGet.Params(StaticRepository.SESSION_TOKEN, login));
    }

    public void newPublicMessage(String text) {
        messagePublicSend.execute(new AsyncCallback<Void>() {
            @Override
            public void onSuccess() {
                getView().showInfoMessage("Success!");
                getView().render();
            }

            @Override
            public void onFailure(Throwable caught) {
                getView().showInfoMessage("Error!");
                getView().render();
            }
        }, new MessagePublicSend.Params(StaticRepository.SESSION_TOKEN, text));
    }

    public void getPublicMessageList(String login) {
        messagePublicListGet.execute(new AsyncCallback<ArrayList<PublicMessage>>() {
            @Override
            public void onSuccess(ArrayList<PublicMessage> messages) {
                getView().showPublicMessageList(messages);
                getView().render();
            }

            @Override
            public void onFailure(Throwable caught) {
                getView().showInfoMessage("No Public Message Found!");
                getView().render();
            }
        }, new MessagePublicListGet.Params(StaticRepository.SESSION_TOKEN, login));
    }


    public interface View extends Presenter.View{
        void render();

        void inputCanceled();

        void renderNewFriend() throws InputCanceledException;

        void renderFriendList();

        void renderNewMessage() throws InputCanceledException;

        void renderMessageList() throws InputCanceledException;

        void renderNewPublicMessage() throws InputCanceledException;

        void showInfoMessage(String message);

        void showFriendList(ArrayList<User> users);

        void showMessageList(ArrayList<Message> messages);

        void renderPublicMessageList() throws InputCanceledException;

        void showPublicMessageList(ArrayList<PublicMessage> messages);
    }
}
