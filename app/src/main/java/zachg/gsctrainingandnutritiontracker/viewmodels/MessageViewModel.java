package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class MessageViewModel extends ViewModel {

    private Message currentMessage;

    public MessageViewModel() {}

    public MessageViewModel(Message message) {
        this.currentMessage = message;
        this.currentMessage.setClientName(message.getClientName());
        this.currentMessage.setBody(message.getBody());
        this.currentMessage.setTitle(message.getTitle());
        this.currentMessage.setDate(message.getDate());
    }

}
