   package app;

   import javax.swing.SwingUtilities;
   import javax.swing.UIManager;
   import vista.VentanaPrincipal;

   public class Main {

       public static void main(String[] args) {
           try {
               UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
           } catch (Exception e) {
               // Si Nimbus no está disponible se usa el aspecto por defecto.
           }
           SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
       }
   }