import Controller.Controller;
import Controller.MyController;
import Model.Model;
import Model.MyModel;
import View.View;
import View.MyView;


public class Cryptoanalyzer {
    //List of String[] params to exchange data between View.View <-> Controller.Controller <-> Model.Model
    //private static final int INPUT_FILE                = 0; //params[0] - input file name
    //private static final int SHIFT                     = 1; //params[1] - shift
    //private static final int OUTPUT_FILE               = 2; //params[2] - output file name
    //private static final int COMMON_WORDS_FILE         = 3; //params[3] - file with common words
    //private static final int EXAMPLE_TEXT_FILE         = 4; //params[4] - file with example text
    //private static final int OPERATION_BEING_PERFORMED = 5; //params[5] - selected (being performed) operation
    //private static final int INTERNAL_MESSAGE          = 6; //params[6] - internal (from method) message

    public static void main(String[] args) {
        String[] params = new String[7];
        DAO.DAO dao = new DAO.FileDAO();
        Model model = new MyModel(params);
        Controller controller = new MyController(dao, model, params);
        View view = new MyView();
        view.letsStart(controller, params);
    }
}