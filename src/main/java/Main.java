import model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args)
    {
        Test.hackathons = new ArrayList<>();

        Partecipante alice          = (Partecipante) Utente.login("alice.rossi@example.com", "Passw0rd!",1);
        Partecipante marco          = (Partecipante) Utente.login("marco.bianchi@example.com", "Sicura123!", 1);
        Partecipante luca           = (Partecipante) Utente.login("luca.verdi@example.com", "P@ssword2024", 1);
        Partecipante giulia         = (Partecipante) Utente.login("giulia.neri@example.com", "Login!2025", 1);

        Organizzatore dardano       = (Organizzatore) Utente.login("giulio.dardano@example.com", "1somorfismo!", 2);
        Giudice antonio             = (Giudice) Utente.login("antonio.pocomento@example.com", "ioHoFortun4!", 3);

        // Creazione della sede
        Sede sede = new Sede("Politecnico di Milano", "Piazza Leonardo da Vinci, 32", 80001);

        // Creazione delle date
        Calendar cal = Calendar.getInstance();

        cal.set(2025, Calendar.JUNE, 15); // 15 giugno 2025
        Date dataInizio = cal.getTime();

        cal.set(2025, Calendar.JUNE, 17); // 17 giugno 2025
        Date dataFine = cal.getTime();

        // Creazione dell'hackathon
        Hackathon hackathon = new Hackathon(
                "HackTheFuture 2025",
                sede,
                4,               // Dimensione team
                100,             // Max iscritti
                dataInizio,
                dataFine
        );

        Test.hackathons.add(hackathon);

        dardano.apreRegistrazioni(Test.hackathons.get(0), true);

        alice.iscrizioneHackathon(hackathon);
        marco.iscrizioneHackathon(hackathon);
        luca.iscrizioneHackathon(hackathon);
        giulia.iscrizioneHackathon(hackathon);

        Team unina = alice.creaTeam("Unina", hackathon);
        Team eureka = luca.creaTeam("Eureka", hackathon);

        alice.iscrizioneTeam(unina, hackathon);
        marco.iscrizioneTeam(unina, hackathon);
        luca.iscrizioneTeam(eureka, hackathon);
        giulia.iscrizioneTeam(eureka, hackathon);

        dardano.apreRegistrazioni(Test.hackathons.get(0), false);

        antonio.pubblicaProblema(hackathon, "LOLLOL");

        unina.caricaDocumento(new Documento("BLA BLA BLA BLI BLI BLI BLU BLUI BLU SALAMALEKUM MALEKUM SALAAAAM, LINGA GULIGULIGULI WATAAAA LINGA GUUUU LINGA GUUU"));

        antonio.commentaDocumento(unina.getDocumenti().get(0), "fa schifo");

        antonio.assegnaVoto(0, unina);
        antonio.assegnaVoto(300, eureka);

        Classifica classifica = hackathon.creaClassifica();

        Test.printAll();
    }
}