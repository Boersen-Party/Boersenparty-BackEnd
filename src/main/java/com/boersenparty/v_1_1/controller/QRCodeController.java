package com.boersenparty.v_1_1.controller;


import com.boersenparty.v_1_1.models.Party;
import com.boersenparty.v_1_1.repository.PartyRepository;
import com.boersenparty.v_1_1.utils.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class QRCodeController {

    @Autowired
    private final PartyRepository partyRepository;

    public QRCodeController(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    //TODO: adjust the url
    //@PreAuthorize("hasAuthority('_PERSONAL')")
    @GetMapping("/parties/{partyId}/qrcodes")
    public ResponseEntity<String> getQRCode(@PathVariable Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

        try {
            byte[] qrCodeBytes = QRCodeService.generateQRCode(party.getAccessCode());

            String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);
            System.out.println("String about to be recieved is: " + qrCodeBase64);
            System.out.println("String about to be recieved is: " + qrCodeBase64);

            System.out.println("String about to be recieved is: " + qrCodeBase64);

            System.out.println("String about to be recieved is: " + qrCodeBase64);


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain") // Or application/json if returning JSON
                    .body(qrCodeBase64);
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }

}
