package app;

import gui.util.FrameManager;

import javax.swing.*;

import gui.Login;
import util.EDatabaseType;

public class Main {

    public static void main(String[] args)
    {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 740);
        frame.setTitle("Login");

        FrameManager frameManager = new FrameManager(frame, EDatabaseType.POSTGRESQL);
        /*Test.hackathons = new ArrayList<>();

        Partecipante alice          = (Partecipante) Utente.login("Alice","Rossi", "alice.rossi@example.com", "1234", 1);
        Partecipante marco          = (Partecipante) Utente.login("Marco","Bianchi", "marco.bianchi@example.com", "Sicura123!", 1);
        Partecipante luca           = (Partecipante) Utente.login("Luca","Verdi","luca.verdi@example.com", "P@ssword2024", 1);
            Partecipante giulia         = (Partecipante) Utente.login("Giulia","Neri", "giulia.neri@example.com", "Login!2025", 1);

        Organizzatore dardano       = (Organizzatore) Utente.login("Giulio","Dardano","giulio.dardano@example.com", "1somorfismo!", 2);
        Giudice antonio             = (Giudice) Utente.login("Antonio","Poco ","antonio.pocomento@example.com", "ioHoFortun4!", 3);

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
        dardano.invitaGiudice(hackathon, antonio);

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

        // ---- //
        antonio.pubblicaProblema(hackathon, "Bug Bounty");

        unina.caricaDocumento(new Documento("Documento"));

        antonio.commentaDocumento(unina.getDocumenti().get(0), "Insufficiente");

        antonio.assegnaVoto(0, unina);
        antonio.assegnaVoto(300, eureka);

        Classifica classifica = hackathon.creaClassifica();

        Test.printAll();*/
        //frame.setContentPane(new Register(controller, frame).$$$getRootComponent$$$()); [OLD--------------------------]

        /*for(Hackathon h : controller.getAllHackathons())
        {
            System.out.println(h.getTitolo());
            for(Team t : h.getTeams())
            {
                System.out.println("\t" + t.getNome()+":");
                for(Partecipante p : t.getPartecipanti())
                {
                    System.out.println("\t\t" + p.getNome() + " " + p.getEmail());
                }
            }
        }

        for(Organizzatore o : controller.getAllOrganizzatoriUsers())
        {
            System.out.println(o.getNome()+" "+o.getEmail());
        }

        for(Giudice g : controller.getAllGiudiciUsers())
        {
            System.out.println(g.getNome()+" "+g.getEmail());
        }*/

        frameManager.switchFrame(new Login(frameManager.getController(), frame));
        frame.setVisible(true);
    }
}