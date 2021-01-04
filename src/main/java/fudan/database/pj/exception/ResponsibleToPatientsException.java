package fudan.database.pj.exception;

/**
 * @author YHT
 */
public class ResponsibleToPatientsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResponsibleToPatientsException(String username) {
        super("WardNurse " + username + " is still responsible to some patients, can't be deleted！");
    }
}
