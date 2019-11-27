package demo.app.adcharge.eu.sdkdemo.Utilites;


import eu.adcharge.api.entities.AdSession;

public class SessionHolder {
    private static AdSession session;

    public static AdSession read(String id) {
        if (id.equals(session.getSession_id()))
            return session;
        return null;
    }

    public static void save(AdSession session) {
        SessionHolder.session = session;
    }
}