package vista;

import estructura.ListaBidireccional;
import estructura.ListaVaciaException;
import estructura.ResultadoBusqueda;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import modelo.Cancion;

/** Capa de interfaz gráfica. Solo llama a métodos públicos del TDA. */
public class VentanaPrincipal extends JFrame {

    private final ListaBidireccional lista = new ListaBidireccional();
    private final ModeloTablaCanciones modeloTabla = new ModeloTablaCanciones(lista);

    private final JTextField txtTitulo = new JTextField();
    private final JTextField txtArtista = new JTextField();
    private final JTextField txtDuracion = new JTextField();
    private final JTextField txtPosicion = new JTextField();
    private final JTextField txtBuscar = new JTextField();

    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblSentido = new JLabel();
    private final JTextArea txtAdelante = crearAreaEnlaces();
    private final JTextArea txtAtras = crearAreaEnlaces();
    private final JLabel lblEstado = new JLabel(" ");

    public VentanaPrincipal() {
        super("Playlist — Lista Bidireccional (TDA con nodos)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        refrescarVista();
        setSize(1150, 720);
        setLocationRelativeTo(null);
    }

    // ===================== CONSTRUCCIÓN DE LA GUI =====================

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la canción"));
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Artista:"));
        panel.add(txtArtista);
        panel.add(new JLabel("Duración (min:seg):"));
        panel.add(txtDuracion);
        panel.add(new JLabel("Posición (0 = primero):"));
        panel.add(txtPosicion);
        panel.add(new JLabel("Título a buscar:"));
        panel.add(txtBuscar);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        return panel;
    }

    private JTextArea crearAreaEnlaces() {
        JTextArea area = new JTextArea(2, 40);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        return area;
    }

    private JPanel crearPanelCentral() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        lblSentido.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.add(lblSentido, BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelAdelante = new JPanel(new BorderLayout());
        panelAdelante.setBorder(BorderFactory.createTitledBorder("Recorrido adelante (enlaces siguiente)"));
        panelAdelante.add(new JScrollPane(txtAdelante), BorderLayout.CENTER);

        JPanel panelAtras = new JPanel(new BorderLayout());
        panelAtras.setBorder(BorderFactory.createTitledBorder("Recorrido atrás (enlaces anterior)"));
        panelAtras.add(new JScrollPane(txtAtras), BorderLayout.CENTER);

        JPanel panelEnlaces = new JPanel(new GridLayout(2, 1, 5, 5));
        panelEnlaces.add(panelAdelante);
        panelEnlaces.add(panelAtras);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelEnlaces, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel filaBotones = new JPanel(new GridLayout(1, 6, 8, 8));
        filaBotones.add(grupo("Insertar",
                boton("Al principio", e -> ejecutar(this::insertarAlPrincipio)),
                boton("Al final", e -> ejecutar(this::insertarAlFinal)),
                boton("En posición", e -> ejecutar(this::insertarEnPosicion))));
        filaBotones.add(grupo("Remover",
                boton("Al principio", e -> ejecutar(this::removerAlPrincipio)),
                boton("Al final", e -> ejecutar(this::removerAlFinal)),
                boton("En posición", e -> ejecutar(this::removerEnPosicion))));
        filaBotones.add(grupo("Mostrar",
                boton("Al principio", e -> ejecutar(this::mostrarPrimero)),
                boton("Al final", e -> ejecutar(this::mostrarUltimo)),
                boton("En posición", e -> ejecutar(this::mostrarEnPosicion))));
        filaBotones.add(grupo("Buscar",
                boton("Por título", e -> ejecutar(this::buscarPorTitulo)),
                boton("Por posición", e -> ejecutar(this::buscarPorPosicion))));
        filaBotones.add(grupo("Recorrer tabla",
                boton("Adelante", e -> cambiarSentido(false)),
                boton("Atrás", e -> cambiarSentido(true))));
        filaBotones.add(grupo("Otros",
                boton("Vaciar lista", e -> ejecutar(this::vaciar)),
                boton("Limpiar campos", e -> limpiarCampos())));

        lblEstado.setBorder(BorderFactory.createEmptyBorder(0, 8, 5, 8));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(filaBotones, BorderLayout.CENTER);
        panel.add(lblEstado, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel grupo(String titulo, JButton... botones) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 4, 4));
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        for (JButton b : botones) {
            panel.add(b);
        }
        return panel;
    }

    private JButton boton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        return boton;
    }

    // ===================== ACCIONES =====================

    private void insertarAlPrincipio() {
        Cancion c = leerCancion();
        lista.insertarAlPrincipio(c);
        limpiarCancion();
        estado("Insertada al principio: " + c.titulo());
    }

    private void insertarAlFinal() {
        Cancion c = leerCancion();
        lista.insertarAlFinal(c);
        limpiarCancion();
        estado("Insertada al final: " + c.titulo());
    }

    private void insertarEnPosicion() {
        Cancion c = leerCancion();
        int posicion = leerPosicion();
        lista.insertarEnPosicion(c, posicion);
        limpiarCancion();
        estado("Insertada en la posición " + posicion + ": " + c.titulo());
    }

    private void removerAlPrincipio() {
        Cancion c = lista.removerAlPrincipio();
        estado("Removida del principio: " + c.titulo());
    }

    private void removerAlFinal() {
        Cancion c = lista.removerAlFinal();
        estado("Removida del final: " + c.titulo());
    }

    private void removerEnPosicion() {
        int posicion = leerPosicion();
        Cancion c = lista.removerEnPosicion(posicion);
        estado("Removida de la posición " + posicion + ": " + c.titulo());
    }

    private void mostrarPrimero() {
        mostrarCancion("Canción al principio (primero)", lista.mostrarPrimero(), 0, "");
    }

    private void mostrarUltimo() {
        mostrarCancion("Canción al final (ultimo)", lista.mostrarUltimo(), lista.tamanio() - 1, "");
    }

    private void mostrarEnPosicion() {
        int posicion = leerPosicion();
        mostrarCancion("Canción en la posición " + posicion, lista.mostrarEnPosicion(posicion), posicion, "");
    }

    private void buscarPorTitulo() {
        String titulo = txtBuscar.getText().trim();
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("Escribe el título a buscar.");
        }
        ResultadoBusqueda r = lista.buscarPorTitulo(titulo);
        if (r == null) {
            tabla.clearSelection();
            JOptionPane.showMessageDialog(this,
                    "No existe ninguna canción con el título \"" + titulo + "\".",
                    "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
            estado("Búsqueda sin resultados: " + titulo);
            return;
        }
        mostrarCancion("Encontrada en la posición " + r.posicion(), r.cancion(), r.posicion(),
                "%n%nRecorrido desde primero con " + r.saltos() + " salto(s).");
    }

    private void buscarPorPosicion() {
        int posicion = leerPosicion();
        ResultadoBusqueda r = lista.buscarPorPosicion(posicion);
        mostrarCancion("Búsqueda por posición " + r.posicion(), r.cancion(), r.posicion(),
                "%n%nRecorrido desde " + r.recorridoDesde() + " con " + r.saltos() + " salto(s).");
    }

    private void vaciar() {
        if (lista.estaVacia()) {
            JOptionPane.showMessageDialog(this, "La lista ya está vacía.",
                    "Vaciar lista", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar las " + lista.tamanio() + " canciones?",
                "Vaciar lista", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            lista.vaciar();
            estado("Lista vaciada: primero = null, ultimo = null");
        }
    }

    private void cambiarSentido(boolean haciaAtras) {
        modeloTabla.setHaciaAtras(haciaAtras);
        refrescarVista();
        estado(haciaAtras ? "Tabla recorrida hacia atrás (desde ultimo)" : "Tabla recorrida hacia adelante (desde primero)");
    }

    // ===================== LECTURA Y VALIDACIÓN DE CAMPOS =====================

    private Cancion leerCancion() {
        String titulo = txtTitulo.getText().trim();
        String artista = txtArtista.getText().trim();
        String duracionTexto = txtDuracion.getText().trim();

        if (titulo.isEmpty() || artista.isEmpty() || duracionTexto.isEmpty()) {
            throw new IllegalArgumentException("Llena todos los campos: título, artista y duración.");
        }
        return new Cancion(titulo, artista, leerDuracion(duracionTexto));
    }

    /** Convierte "3:45" en 225 segundos. */
    private int leerDuracion(String texto) {
        if (!texto.matches("\\d{1,3}:[0-5]\\d")) {
            throw new IllegalArgumentException("La duración debe tener el formato minutos:segundos. Ejemplo: 3:45");
        }
        int dosPuntos = texto.indexOf(':');
        int minutos = Integer.parseInt(texto.substring(0, dosPuntos));
        int segundos = Integer.parseInt(texto.substring(dosPuntos + 1));
        return minutos * 60 + segundos;
    }

    private int leerPosicion() {
        String texto = txtPosicion.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Escribe la posición (0 = primero).");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La posición debe ser un número entero. Ejemplo: 2");
        }
    }

    // ===================== UTILIDADES DE LA VISTA =====================

    /** Ejecuta una acción y convierte cualquier error en un aviso amigable. */
    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (ListaVaciaException e) {
            aviso("Lista vacía", e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            aviso("Posición fuera de rango", e.getMessage());
        } catch (IllegalArgumentException e) {
            aviso("Dato inválido", e.getMessage());
        }
        refrescarVista();
    }

    private void mostrarCancion(String titulo, Cancion c, int posicion, String extra) {
        refrescarVista();
        int fila = modeloTabla.convertir(posicion);
        tabla.setRowSelectionInterval(fila, fila);
        tabla.scrollRectToVisible(tabla.getCellRect(fila, 0, true));
        JOptionPane.showMessageDialog(this, c.describir() + String.format(extra), titulo,
                JOptionPane.INFORMATION_MESSAGE);
        estado(titulo + ": " + c.titulo());
    }

    private void refrescarVista() {
        modeloTabla.refrescar();
        txtAdelante.setText(lista.recorrerAdelante());
        txtAtras.setText(lista.recorrerAtras());
        lblSentido.setText(modeloTabla.isHaciaAtras()
                ? "Tabla en sentido inverso: de ultimo a primero"
                : "Tabla en sentido directo: de primero a ultimo");
        setTitle("Playlist — Lista Bidireccional — " + lista.tamanio() + " canción(es)");
    }

    private void aviso(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
        estado(mensaje);
    }

    private void estado(String mensaje) {
        lblEstado.setText(mensaje);
    }

    private void limpiarCancion() {
        txtTitulo.setText("");
        txtArtista.setText("");
        txtDuracion.setText("");
        txtTitulo.requestFocus();
    }

    private void limpiarCampos() {
        limpiarCancion();
        txtPosicion.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
        estado(" ");
    }
}