package ulb.views.accounts;


/**
 * ChooseLanguageView is responsible for displaying the language selection options to the user.
 * It provides buttons for selecting different languages and communicates the user's choice to the controller.
 */
public class ChooseLanguageView {
    private ChooseLanguageViewListener listener;

    public void setListener(ChooseLanguageViewListener listener) {
        this.listener = listener;
    }

    public void handleChangeToFrench() {
        listener.changeToFrench();
    }

    public void handleChangeToEnglish() {
        listener.changeToEnglish();
    }

    public void handleChangeToDutch() {
        listener.changeToDutch();
    }

    public interface ChooseLanguageViewListener {
        void changeToFrench();

        void changeToEnglish();

        void changeToDutch();
    }
}
