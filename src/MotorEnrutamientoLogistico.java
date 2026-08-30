import java.util.*;

public class MotorEnrutamientoLogistico {

    // Nombres de las sedes según el enunciado del proyecto
    private static final String[] NOMBRES_SEDES = {
            "QUITO", "MANTA", "GUAYAQUIL", "AMBATO", "CUENCA"
    };

    // Matriz de adyacencia con los costos (distancias en km)
    // 0: Quito, 1: Manta, 2: Guayaquil, 3: Ambato, 4: Cuenca
    private static final int INF = 999999;
    private static final int[][] GRAFO = {
            //         QIT  MNT  GYE  AMB  CUE
            /* QIT */ {  0, INF, 420, 150, INF },
            /* MNT */ { INF,   0, 190, 310, INF },
            /* GYE */ { 420, 190,   0, INF, 195 },
            /* AMB */ { 150, 310, INF,   0, 220 },
            /* CUE */ { INF, INF, 195, 220,   0 }
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACIÓN");
        System.out.println("     Estudiante: Kihuaw Chaves");
        System.out.println("==================================================");
        System.out.println("Sedes disponibles:");
        for (int i = 0; i < NOMBRES_SEDES.length; i++) {
            System.out.println("[" + i + "] " + NOMBRES_SEDES[i]);
        }
        System.out.println("==================================================");

        System.out.print("Ingrese el ID de la Sede de Origen (0-4): ");
        int origen = scanner.nextInt();

        System.out.print("Ingrese el ID de la Sede de Destino (0-4): ");
        int destino = scanner.nextInt();

        if (origen < 0 || origen > 4 || destino < 0 || destino > 4) {
            System.out.println("Error: IDs de sede inválidos. Deben estar entre 0 y 4.");
            scanner.close();
            return;
        }

        resolverDijkstra(origen, destino);
        scanner.close();
    }

    public static void resolverDijkstra(int origen, int destino) {
        int n = GRAFO.length;
        int[] distancias = new int[n];
        boolean[] visitados = new boolean[n];
        int[] padres = new int[n];

        Arrays.fill(distancias, INF);
        Arrays.fill(padres, -1);
        distancias[origen] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = obtenerMinimaDistancia(distancias, visitados);
            if (u == -1) break;
            visitados[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visitados[v] && GRAFO[u][v] != INF && distancias[u] + GRAFO[u][v] < distancias[v]) {
                    distancias[v] = distancias[u] + GRAFO[u][v];
                    padres[v] = u;
                }
            }
        }

        // Reconstruir la ruta óptima de atrás hacia adelante
        List<Integer> ruta = new ArrayList<>();
        int actual = destino;
        while (actual != -1) {
            ruta.add(0, actual);
            actual = padres[actual];
        }

        imprimirSalidaCLI(origen, destino, distancias[destino], ruta);
    }

    private static int obtenerMinimaDistancia(int[] distancias, boolean[] visitados) {
        int min = INF, minIndex = -1;
        for (int v = 0; v < distancias.length; v++) {
            if (!visitados[v] && distancias[v] <= min) {
                min = distancias[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private static void imprimirSalidaCLI(int origen, int destino, int distanciaTotal, List<Integer> ruta) {
        System.out.println("\n==================================================");
        System.out.println("[Ruta seleccionada]: " + NOMBRES_SEDES[origen] + " (" + origen + ") -> " + NOMBRES_SEDES[destino] + " (" + destino + ")");

        System.out.println("[Gráfica de la ruta óptima]:");
        StringBuilder grafica = new StringBuilder();
        StringBuilder secuenciaNombres = new StringBuilder();

        for (int i = 0; i < ruta.size(); i++) {
            int nodo = ruta.get(i);
            grafica.append("[").append(nodo).append("] ").append(NOMBRES_SEDES[nodo]);
            secuenciaNombres.append(NOMBRES_SEDES[nodo]);

            if (i < ruta.size() - 1) {
                int siguiente = ruta.get(i + 1);
                int peso = GRAFO[nodo][siguiente];
                grafica.append("-(").append(peso).append(" km)-> ");
                secuenciaNombres.append(" -> ");
            }
        }

        System.out.println(grafica.toString());
        System.out.println("[Detalle del Despacho]:");
        System.out.println("• Origen:          " + NOMBRES_SEDES[origen] + " [Sede " + origen + "]");
        System.out.println("• Destino:         " + NOMBRES_SEDES[destino] + " [Sede " + destino + "]");
        System.out.println("• Secuencia Óptima: " + secuenciaNombres.toString());
        System.out.println("• Distancia Total: " + distanciaTotal + " km");
        System.out.println("==================================================");
    }
}