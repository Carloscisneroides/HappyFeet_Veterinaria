package com.happyfeet.config;

import com.happyfeet.controller.*;
import com.happyfeet.repository.*;
import com.happyfeet.repository.impl.*;
import com.happyfeet.service.*;
import com.happyfeet.service.impl.*;
import com.happyfeet.view.*;

public class DependencyFactory {

    private static DependencyFactory instance;

    // Repositories
    private CitaRepository citaRepository;
    private DuenoRepository duenoRepository;
    private MascotaRepository mascotaRepository;
    private FacturaRepository facturaRepository;
    private InventarioRepository inventarioRepository;

    // Services
    private CitaService citaService;
    private DuenoService duenoService;
    private MascotaService mascotaService;
    private FacturaService facturaService;
    private InventarioService inventarioService;
    private ReporteService reporteService;
    private VeterinarioService veterinarioService;

    // Views
    private CitaView citaView;
    private DuenoView duenoView;
    private MascotaView mascotaView;

    // Controllers
    private CitaController citaController;
    private DuenoController duenoController;
    private MascotaController mascotaController;
    private FacturaController facturaController;
    private InventarioController inventarioController;
    private ReporteController reporteController;
    private ActividadesEspecialesController actividadesController;
    private MainController mainController;

    private DependencyFactory() {
        initializeComponents();
    }

    public static DependencyFactory getInstance() {
        if (instance == null) {
            instance = new DependencyFactory();
        }
        return instance;
    }

    private void initializeComponents() {
        // Initialize repositories
        citaRepository = new CitaRepositoryImpl();
        duenoRepository = new DuenoRepositoryImpl();
        mascotaRepository = new MascotaRepositoryImpl();
        facturaRepository = new FacturaRepositoryImpl();
        inventarioRepository = new InventarioRepositoryImpl();

        // Initialize services
        duenoService = new DuenoServiceImpl(duenoRepository);
        mascotaService = new MascotaServiceImpl(mascotaRepository);
        citaService = new CitaServiceImpl(citaRepository);
        facturaService = new FacturaServiceImpl(facturaRepository);
        inventarioService = new InventarioService(inventarioRepository);
        reporteService = new ReporteServiceImpl(new LoggerServiceImpl());

        // Create a basic veterinario service implementation
        veterinarioService = new VeterinarioService() {
            @Override
            public java.util.Optional<com.happyfeet.model.entities.Veterinario> buscarPorId(Long id) {
                return java.util.Optional.empty();
            }

            @Override
            public boolean existePorId(Long id) {
                return false;
            }
        };

        // Initialize views
        citaView = new CitaView();
        duenoView = new DuenoView();
        mascotaView = new MascotaView();

        // Initialize controllers with dependencies
        citaController = new CitaController(citaService, mascotaService, veterinarioService, citaView);
        duenoController = new DuenoController(duenoService, mascotaService, duenoView);
        mascotaController = new MascotaController(mascotaService, duenoService, mascotaView);
        facturaController = new FacturaController(facturaService, duenoService, inventarioService, inventarioRepository);
        inventarioController = new InventarioController(inventarioService, inventarioRepository);
        reporteController = new ReporteController(facturaService, mascotaService, duenoService, citaService, inventarioRepository);
        actividadesController = new ActividadesEspecialesController(mascotaService, duenoService);
        mainController = new MainController();
    }

    // Getters for controllers
    public CitaController getCitaController() {
        return citaController;
    }

    public DuenoController getDuenoController() {
        return duenoController;
    }

    public MascotaController getMascotaController() {
        return mascotaController;
    }

    public FacturaController getFacturaController() {
        return facturaController;
    }

    public InventarioController getInventarioController() {
        return inventarioController;
    }

    public ReporteController getReporteController() {
        return reporteController;
    }

    public ActividadesEspecialesController getActividadesController() {
        return actividadesController;
    }

    public MainController getMainController() {
        return mainController;
    }

    // Getters for services (if needed elsewhere)
    public CitaService getCitaService() {
        return citaService;
    }

    public DuenoService getDuenoService() {
        return duenoService;
    }

    public MascotaService getMascotaService() {
        return mascotaService;
    }

    public FacturaService getFacturaService() {
        return facturaService;
    }

    public InventarioService getInventarioService() {
        return inventarioService;
    }

    public ReporteService getReporteService() {
        return reporteService;
    }
}