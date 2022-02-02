package com.example.demo;

//import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
//import com.esri.arcgisruntime.concurrent.ListenableFuture;
//import com.esri.arcgisruntime.geometry.Point;
//import com.esri.arcgisruntime.geometry.SpatialReferences;
//import com.esri.arcgisruntime.mapping.ArcGISMap;
//import com.esri.arcgisruntime.mapping.BasemapStyle;
//import com.esri.arcgisruntime.mapping.Viewpoint;
//import com.esri.arcgisruntime.mapping.view.MapView;
//import com.esri.arcgisruntime.tasks.networkanalysis.*;
import com.example.demo.models.*;
import com.example.demo.services.*;
import com.example.demo.validators.RequestValidator;
import com.example.demo.validators.TruckValidator;
import com.example.demo.validators.UserValidator;
//import com.google.cloud.Tuple;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;

//import javafx.geometry.Point2D;
//import javafx.util.Pair;
import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

@Controller
@AllArgsConstructor
public class CRUDController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private TrucksService trucksService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private SecurityServiceImpl securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private TruckValidator truckValidator;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PDFGeneratorServices pdfGeneratorServices;


    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/home";
        }

        model.addAttribute("userForm", new Users());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") Users userForm, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userForm.setPassword(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userForm.setPasswordConfirm(userForm.getPassword());
        usersService.createUser(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/home";
        }

        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model) throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getUser(userDetails.getUsername());

        model.addAttribute("currentUserForm", user);

        return "profile";
    }

    @PostMapping("/profile")
    public String trucks(Model model, @ModelAttribute("currentUserForm") Users currentUserForm, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getUser(userDetails.getUsername());

        currentUserForm.setId(user.getId());
        currentUserForm.setUsername(user.getUsername());

        if (!bCryptPasswordEncoder.matches(currentUserForm.getPassword(), user.getPassword()) && currentUserForm.getPassword() != null) {
            currentUserForm.setPassword(bCryptPasswordEncoder.encode(currentUserForm.getPassword()));
            currentUserForm.setPasswordConfirm(currentUserForm.getPassword());
        } else {
            currentUserForm.setPassword(user.getPassword());
            currentUserForm.setPasswordConfirm(user.getPasswordConfirm());
        }

        currentUserForm.setType(user.getType());

//        userValidator.validate(currentUserForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "profile";
        }

        usersService.updateUser(currentUserForm);

        model.addAttribute("currentUserForm", currentUserForm);

        return "profile";
    }

    @GetMapping("/orders")
    public String orders(Model model) throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestsList = requestsService.getRequests(usersService.getUser(userDetails.getUsername()).getId());
        requestsList.sort(Comparator.comparing(Requests::getId));

        model.addAttribute("requestForm", new Requests());
        model.addAttribute("allRequestsForm", requestsList);
        model.addAttribute("requestsService", requestsService);

        return "orders";
    }

    @PostMapping("/orders")
    public String orders(Model model, @ModelAttribute("requestForm") Requests requestForm, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        requestValidator.validate(requestForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "orders";
        }

        requestsService.createRequest(requestForm);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestsList = requestsService.getRequests(usersService.getUser(userDetails.getUsername()).getId());
        requestsList.sort(Comparator.comparing(Requests::getId));

        model.addAttribute("allRequestsForm", requestsList);

        return "orders";
    }

    @RequestMapping(value = "/delete_order", method = RequestMethod.GET)
    public String handleDeleteRequest(Model model, @RequestParam(name="Id") String Id) throws ExecutionException, InterruptedException {
        requestsService.deleteRequest(Id);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Requests> requestsList = requestsService.getRequests(usersService.getUser(userDetails.getUsername()).getId());
        requestsList.sort(Comparator.comparing(Requests::getId));

        model.addAttribute("allRequestsForm", requestsList);

        return "redirect:/orders";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) throws ExecutionException, InterruptedException {
        List<Requests> requestsList = requestsService.getAllRequests();
        requestsList.sort(Comparator.comparing(Requests::getId));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Trucks> trucksList = trucksService.getTrucks(usersService.getUser(userDetails.getUsername()).getId());
        trucksList.sort(Comparator.comparing(Trucks::getId));

//        ArcGISRuntimeEnvironment.setApiKey("AAPKfc023320ea5b4fb9955d7e74a5623fb4KZHfa6kn7i--Jv-1qxWZfL067XASOUpPONXgOqcnsQU8AgpZer-JDLilHLOJL9pY");
//
//        MapView mapView = new MapView();
//        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
//        mapView.setMap(map);
//        mapView.setViewpoint(new Viewpoint(34.05398, -118.24532, 144447.638572));

//        RouteTask routeTask = new RouteTask("https://route-api.arcgis.com/arcgis/rest/services/World/Route/NAServer/Route_World");
//        RouteParameters routeParameters = new RouteParameters();
//        List<Stop> routeStops = new ArrayList<>();
//        Point point1, point2;

        List<Jobs> matchingRequestsList = new ArrayList<>();

        for (Requests request : requestsList) {
            for (Trucks truck : trucksList) {
//                AtomicReference<Double> routeLength = new AtomicReference<>();
//
//                point1 = new Point(request.getSource_lng(), request.getSource_lat(), SpatialReferences.getWgs84());
//                point2 = new Point(truck.getLng(), truck.getLat(), SpatialReferences.getWgs84());
//                System.out.println(point1.getX() + " " + point1.getY());
//                routeStops.add(new Stop(point1));
//                routeStops.add(new Stop(point2));
//                routeParameters.setStops(routeStops);
//                ListenableFuture<RouteResult> routeResultFuture = routeTask.solveRouteAsync(routeParameters);
//                System.out.println(routeResultFuture.get(10, TimeUnit.SECONDS).getRoutes().get(0).getTotalLength());
//                routeResultFuture.addDoneListener(() -> {
//                    try {
//                        RouteResult routeResult = routeResultFuture.get();
//                        Route route = routeResult.getRoutes().get(0);
//                        routeLength.set(route.getTotalLength());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//                sleep(10000);
//                System.out.println(routeLength.get());

                //double distance = Jobs.getDistance(request.getSource_lat(), request.getSource_lng(), truck.getLat(), truck.getLng());

                if (request.getVolume() <= truck.getVolume() &&
                        request.getWeight() <= truck.getWeight() &&
                        truck.getAvailability().equals("true") &&
                        Jobs.calculateProfit(request, truck) > 0) {
                    matchingRequestsList.add(new Jobs(request, truck));
                }
            }
        }

        matchingRequestsList.sort(new Comparator<Jobs>() {
            public int compare(Jobs job1, Jobs job2) {
                return Double.compare(job1.getProfit(), job2.getProfit());
            }
        });

        model.addAttribute("allJobsForm", matchingRequestsList);

        return "jobs";
    }

    @GetMapping("/contracts")
    public String contracts(Model model) throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = usersService.getUser(userDetails.getUsername());
        String type = user.getType();
        List<Contract> contractList = contractService.getContracts(usersService.getUser(userDetails.getUsername()).getId(), type);
        contractList.sort(Comparator.comparing(Contract::getId));

        model.addAttribute("allContractsForm", contractList);
        model.addAttribute("contractService", contractService);

        return "contracts";
    }

    @RequestMapping(value = "/export_pdf", method = RequestMethod.GET)
    public void handleExportPdf(HttpServletResponse response, @RequestParam(name="contract_Id") String contractID) throws ExecutionException, InterruptedException, DocumentException {
        Contract contract = contractService.getContract(contractID);

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorServices.export(response, contract);
    }

    @RequestMapping(value = "/create_contract", method = RequestMethod.GET)
    public String handleCreateContract(Model model,  @RequestParam(name="request_Id") String request_id, @RequestParam(name="truck_Id") String truck_id) throws ExecutionException, InterruptedException {
        Contract contract = new Contract();

        Requests request = requestsService.getRequest(request_id);
        Trucks truck = trucksService.getTruck(truck_id);

        truck.setAvailability("false");
        trucksService.updateTruck(truck);

        contract.setId_request(request.getId());
        contract.setId_truck(truck.getId());
        contract.setId_sender(request.getId_sender());
        contract.setId_transporter(truck.getIdTransporters());
        contract.setDestination_lat(request.getDestination_lat());
        contract.setDestination_lng(request.getDestination_lng());
        contract.setSource_lat(request.getSource_lat());
        contract.setSource_lng(request.getSource_lng());
        contract.setDistance_free(Jobs.calculateDistance(truck.getLat(), truck.getLng(), request.getDestination_lat(), request.getDestination_lng()));
        contract.setDistance_full(Jobs.calculateDistance(request.getDestination_lat(), request.getDestination_lng(), request.getSource_lat(), request.getSource_lng()));
        contract.setTypeGoods(request.getTypeGoods());
        contract.setPayTerm(request.getDateStopMax());
        contract.setVolume(request.getVolume());
        contract.setWeight(request.getWeight());
        contract.setTotalCost(request.getMoney());

        requestsService.deleteRequest(request.getId().toString());

        contractService.createContract(contract);

        List<Requests> requestsList = requestsService.getAllRequests();
        requestsList.sort(Comparator.comparing(Requests::getId));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Trucks> trucksList = trucksService.getTrucks(usersService.getUser(userDetails.getUsername()).getId());
        trucksList.sort(Comparator.comparing(Trucks::getId));

        List<Jobs> matchingRequestsList = new ArrayList<>();

        for (Requests r : requestsList) {
            for (Trucks t : trucksList) {
                if (r.getVolume() <= t.getVolume() &&
                        r.getWeight() <= t.getWeight() &&
                        t.getAvailability().equals("true") &&
                        Jobs.calculateProfit(r, t) > 0) {
                    matchingRequestsList.add(new Jobs(r, t));
                }
            }
        }

        matchingRequestsList.sort(new Comparator<Jobs>() {
            public int compare(Jobs job1, Jobs job2) {
                return Double.compare(job1.getProfit(), job2.getProfit());
            }
        });

        model.addAttribute("allJobsForm", matchingRequestsList);

        return "redirect:/jobs";
    }

    @RequestMapping(value = "/job_view", method = RequestMethod.GET)
    public String handleViewJob(Model model, @RequestParam(name="request_Id") String request_id, @RequestParam(name="truck_Id") String truck_id) throws ExecutionException, InterruptedException {
        Requests request = requestsService.getRequest(request_id);
        Trucks truck = trucksService.getTruck(truck_id);

        Jobs job = new Jobs(request, truck);
        model.addAttribute("job", job);

        return "job_view";
    }

    @GetMapping("/trucks")
    public String trucks(Model model) throws ExecutionException, InterruptedException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Trucks> trucksList = trucksService.getTrucks(usersService.getUser(userDetails.getUsername()).getId());
        trucksList.sort(Comparator.comparing(Trucks::getId));

        model.addAttribute("truckForm", new Trucks());
        model.addAttribute("allTrucksForm", trucksList);
        model.addAttribute("trucksService", trucksService);

        return "trucks";
    }

    @PostMapping("/trucks")
    public String trucks(Model model, @ModelAttribute("truckForm") Trucks truckForm, BindingResult bindingResult) throws ExecutionException, InterruptedException {
        truckValidator.validate(truckForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "trucks";
        }

        trucksService.createTruck(truckForm);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Trucks> trucksList = trucksService.getTrucks(usersService.getUser(userDetails.getUsername()).getId());
        trucksList.sort(Comparator.comparing(Trucks::getId));

        model.addAttribute("allTrucksForm", trucksList);

        return "trucks";
    }

    @RequestMapping(value = "/delete_truck", method = RequestMethod.GET)
    public String handleDeleteTruck(Model model, @RequestParam(name="Id") String Id) throws ExecutionException, InterruptedException {
        trucksService.deleteTruck(Id);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Trucks> trucksList = trucksService.getTrucks(usersService.getUser(userDetails.getUsername()).getId());
        trucksList.sort(Comparator.comparing(Trucks::getId));

        model.addAttribute("allTrucksForm", trucksList);

        return "redirect:/trucks";
    }
}
