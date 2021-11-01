package quest_system;

public class AcceptQuest {

    public void accept(Quest quest) {
        assert !quest.isAccepted();

        quest.toggleAccepted();
    }

}