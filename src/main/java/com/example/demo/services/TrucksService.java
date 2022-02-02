package com.example.demo.services;

import com.example.demo.models.Transporters;
import com.example.demo.models.Trucks;
import com.example.demo.models.Users;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TrucksService {
    @Autowired
    private CountersService countersService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TransportersService transportersService;

    public String createTruck(Trucks truck) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        int count = countersService.getCounter("countTruck");
        truck.setId(count + 1);
        truck.setAvailability("true");

        // get id for current user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer idTransporter = usersService.getUser(userDetails.getUsername()).getId();
        truck.setIdTransporters(idTransporter);

        // update counter for trucks
        countersService.updateCounter("countTruck");

        ApiFuture<WriteResult> collectionsApiFuture =  db.collection("Trucks").document(truck.getId().toString()).set(truck);

        CollectionReference transporters = db.collection("Transporters");
        Query query = transporters.whereEqualTo("id_user", idTransporter);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (querySnapshot.get().getDocuments().size() > 0) {
            DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
            Transporters transporter = document.toObject(Transporters.class);

            // increase the total number of trucks
            transportersService.updateTransporter(transporter, true);
        }

        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String updateTruck(Trucks truck) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = db.collection("Trucks").document(truck.getId().toString()).set(truck);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteTruck(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Trucks").document(documentId).delete();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer idTransporter = usersService.getUser(userDetails.getUsername()).getId();

        CollectionReference trucks = db.collection("Transporters");
        Query query = trucks.whereEqualTo("id_user", idTransporter);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
        Transporters transporter = document.toObject(Transporters.class);

        // decrease the total number of trucks
        transportersService.updateTransporter(transporter, false);

        return "Successfully deleted" + documentId;
    }

    public Trucks getTruck(String documentId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Trucks").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Trucks truck;

        if (document.exists()) {
            truck = document.toObject(Trucks.class);
            return truck;
        }

        return null;
    }

    public List<Trucks> getTrucks(Integer idTransporter) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference trucks = db.collection("Trucks");
        Query query = trucks.whereEqualTo("idTransporters", idTransporter);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<Trucks> trucksList = new ArrayList<>();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Trucks truck;
            truck = document.toObject(Trucks.class);
            trucksList.add(truck);
        }

        return trucksList;
    }
}
