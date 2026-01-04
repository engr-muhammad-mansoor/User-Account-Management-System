package com.example.ams.data.transaction_data;

import com.example.ams.data.accounts_data.AccountNotFoundException;
import com.example.ams.data.transaction_data.PurchaseDTO;
import com.example.ams.data.transaction_data.TransactionDTO;
import com.example.ams.data.transaction_data.TransactionService;
import com.example.ams.user.UserNotFoundException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/system-user/deposit")
    public ResponseEntity<?> depositFunds(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO result = transactionService.depositFunds(transactionDTO);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (IllegalCallerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid PIN: " + e.getMessage());
        } catch (AccountNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/system-user/withdraw")
    public ResponseEntity<?> withdrawFunds(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO result = transactionService.withdrawFunds(transactionDTO);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (InsufficientResourcesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance: " + e.getMessage());
        } catch (AccountNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }



    @PostMapping("/system-user/purchase")
    public ResponseEntity<?> purchaseProduct(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            PurchaseDTO result = transactionService.purchaseProduct(purchaseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (InsufficientResourcesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance: " + e.getMessage());
        } catch (AccountNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }


    @GetMapping("/system-user/generate-pdf-transaction")
    public ResponseEntity<byte[]> generatePDF(@RequestBody TransactionDTO transactionDTO) {
        try {
            byte[] pdfContent = generatePDFContentForTransaction(transactionDTO);
            return ResponseEntity
                    .ok()
                    .header("Content-Disposition", "attachment; filename=transaction.pdf")
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF".getBytes());
        }
    }

    private byte[] generatePDFContentForTransaction(TransactionDTO transactionDTO ) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Transaction Details"));

            // Check the type of DTO and display corresponding information
            if (transactionDTO instanceof TransactionDTO) {
                document.add(new Paragraph("Account ID: " + transactionDTO.getAccountId()));
                document.add(new Paragraph("Customer ID: " + transactionDTO.getCustomerId()));
                document.add(new Paragraph("Processed Amount: " + transactionDTO.getProcessedAmount()));
                document.add(new Paragraph("Transaction Type: " + transactionDTO.getTransactionType()));
            }  else {
                throw new IllegalArgumentException("Unsupported DTO type");
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception("Error generating PDF");
        }
    }

    @GetMapping("/system-user/generate-pdf-purchase")
    public ResponseEntity<byte[]> generatePDFForPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            byte[] pdfContent = generatePDFContentForPurchase(purchaseDTO);
            return ResponseEntity
                    .ok()
                    .header("Content-Disposition", "attachment; filename=transaction.pdf")
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF".getBytes());
        }
    }

    private byte[] generatePDFContentForPurchase(PurchaseDTO purchaseDTO) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Purchase Details"));

            // Check the type of DTO and display corresponding information
           if (purchaseDTO instanceof PurchaseDTO) {
                // Handle PurchaseDTO
                document.add(new Paragraph("Account ID: " + purchaseDTO.getAccountId()));
                document.add(new Paragraph("Customer ID: " + purchaseDTO.getCustomerId()));
                document.add(new Paragraph("Processed Amount: " + purchaseDTO.getProcessedAmount()));
                document.add(new Paragraph("Transaction Type: " + purchaseDTO.getTransactionType()));
                document.add(new Paragraph("Product Name: " + purchaseDTO.getProductDescription()));
            } else {
                throw new IllegalArgumentException("Unsupported DTO type");
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception("Error generating PDF");
        }
    }


}
