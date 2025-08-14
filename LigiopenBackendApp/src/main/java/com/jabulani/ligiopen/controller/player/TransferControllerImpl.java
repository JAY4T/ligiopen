package com.jabulani.ligiopen.controller.player;

import com.jabulani.ligiopen.dto.player.TransferRequestDto;
import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.service.player.TransferService;
import com.jabulani.ligiopen.config.web.BuildResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
public class TransferControllerImpl implements TransferController {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferControllerImpl.class);
    
    private final TransferService transferService;
    private final BuildResponse buildResponse;
    
    @Autowired
    public TransferControllerImpl(TransferService transferService, BuildResponse buildResponse) {
        this.transferService = transferService;
        this.buildResponse = buildResponse;
    }
    
    @Override
    public ResponseEntity<Object> createTransferRequest(@Valid TransferRequestDto transferRequestDto) {
        try {
            logger.info("Creating transfer request for player ID: {}", transferRequestDto.getPlayerId());
            
            PlayerTransfer transfer = transferService.createTransferRequest(
                transferRequestDto.getPlayerId(),
                transferRequestDto.getFromClubId(),
                transferRequestDto.getToClubId(),
                transferRequestDto.getTransferType(),
                transferRequestDto.getContractType(),
                transferRequestDto.getTransferFee(),
                transferRequestDto.getProposedSalary(),
                transferRequestDto.getContractDuration(),
                transferRequestDto.getNotes()
            );
            
            return buildResponse.success(transfer, "Transfer request created successfully", null, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            logger.error("Failed to create transfer request: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to create transfer request: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error creating transfer request", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getTransferById(Long transferId) {
        try {
            logger.debug("Fetching transfer by ID: {}", transferId);
            
            Optional<PlayerTransfer> transfer = transferService.getTransferById(transferId);
            if (transfer.isPresent()) {
                return buildResponse.success(transfer.get(), "Transfer retrieved successfully");
            } else {
                Map<String, Object> errors = new HashMap<>();
                errors.put("transfer", "Transfer not found with ID: " + transferId);
                return buildResponse.error("failed", errors, HttpStatus.NOT_FOUND);
            }
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch transfer: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to fetch transfer: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching transfer", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> approveTransfer(Long transferId) {
        try {
            logger.info("Approving transfer ID: {}", transferId);
            
            Long userId = getCurrentUserId();
            PlayerTransfer transfer = transferService.approveTransfer(transferId, userId);
            
            return buildResponse.success(transfer, "Transfer approved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to approve transfer: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to approve transfer: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error approving transfer", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> rejectTransfer(Long transferId, String reason) {
        try {
            logger.info("Rejecting transfer ID: {} with reason: {}", transferId, reason);
            
            Long userId = getCurrentUserId();
            PlayerTransfer transfer = transferService.rejectTransfer(transferId, userId, reason);
            
            return buildResponse.success(transfer, "Transfer rejected successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to reject transfer: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to reject transfer: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error rejecting transfer", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> completeTransfer(Long transferId) {
        try {
            logger.info("Completing transfer ID: {}", transferId);
            
            PlayerTransfer transfer = transferService.completeTransfer(transferId);
            
            return buildResponse.success(transfer, "Transfer completed successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to complete transfer: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to complete transfer: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error completing transfer", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> cancelTransfer(Long transferId, String reason) {
        try {
            logger.info("Cancelling transfer ID: {} with reason: {}", transferId, reason);
            
            Long userId = getCurrentUserId();
            PlayerTransfer transfer = transferService.cancelTransfer(transferId, userId, reason);
            
            return buildResponse.success(transfer, "Transfer cancelled successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to cancel transfer: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfer", "Failed to cancel transfer: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error cancelling transfer", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getPendingTransfersForClub(Long clubId, int page, int size) {
        try {
            logger.debug("Fetching pending transfers for club ID: {} (page: {}, size: {})", clubId, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<PlayerTransfer> transfers = transferService.getPendingTransfersForClub(clubId, pageRequest);
            
            return buildResponse.success(transfers, "Pending transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch pending transfers: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch pending transfers: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching pending transfers", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getIncomingTransfers(Long clubId, int page, int size) {
        try {
            logger.debug("Fetching incoming transfers for club ID: {} (page: {}, size: {})", clubId, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<PlayerTransfer> transfers = transferService.getIncomingTransfers(clubId, pageRequest);
            
            return buildResponse.success(transfers, "Incoming transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch incoming transfers: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch incoming transfers: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching incoming transfers", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getOutgoingTransfers(Long clubId, int page, int size) {
        try {
            logger.debug("Fetching outgoing transfers for club ID: {} (page: {}, size: {})", clubId, page, size);
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<PlayerTransfer> transfers = transferService.getOutgoingTransfers(clubId, pageRequest);
            
            return buildResponse.success(transfers, "Outgoing transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch outgoing transfers: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch outgoing transfers: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching outgoing transfers", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getRecentTransfers(int limit) {
        try {
            logger.debug("Fetching recent transfers (limit: {})", limit);
            
            List<PlayerTransfer> transfers = transferService.getRecentTransfers(limit);
            
            return buildResponse.success(transfers, "Recent transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch recent transfers: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch recent transfers: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching recent transfers", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getTransfersByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        try {
            logger.debug("Fetching transfers by date range: {} to {} (page: {}, size: {})", startDate, endDate, page, size);
            
            if (startDate.isAfter(endDate)) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("dateRange", "Start date cannot be after end date");
                return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
            }
            
            PageRequest pageRequest = PageRequest.of(page, size);
            List<PlayerTransfer> transfers = transferService.getTransfersByDateRange(startDate, endDate, pageRequest);
            
            return buildResponse.success(transfers, "Transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch transfers by date range: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch transfers by date range: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching transfers by date range", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getHighValueTransfers(BigDecimal minimumValue, int limit) {
        try {
            logger.debug("Fetching high-value transfers above {} (limit: {})", minimumValue, limit);
            
            if (minimumValue.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> errors = new HashMap<>();
                errors.put("minimumValue", "Minimum value must be positive");
                return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
            }
            
            List<PlayerTransfer> transfers = transferService.getHighValueTransfers(minimumValue, limit);
            
            return buildResponse.success(transfers, "High-value transfers retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch high-value transfers: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("transfers", "Failed to fetch high-value transfers: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching high-value transfers", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<Object> getClubTransferStatistics(Long clubId) {
        try {
            logger.debug("Fetching transfer statistics for club ID: {}", clubId);
            
            Map<String, Object> statistics = transferService.getClubTransferStatistics(clubId);
            
            return buildResponse.success(statistics, "Transfer statistics retrieved successfully");
            
        } catch (RuntimeException e) {
            logger.error("Failed to fetch transfer statistics: {}", e.getMessage());
            Map<String, Object> errors = new HashMap<>();
            errors.put("statistics", "Failed to fetch transfer statistics: " + e.getMessage());
            return buildResponse.error("failed", errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error fetching transfer statistics", e);
            Map<String, Object> errors = new HashMap<>();
            errors.put("error", "Unexpected error occurred");
            return buildResponse.error("failed", errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private Long getCurrentUserId() {
        try {
            // Extract user ID from security context
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof Long) {
                return (Long) principal;
            } else if (principal instanceof String) {
                return Long.parseLong((String) principal);
            } else {
                logger.warn("Unable to extract user ID from security context, using default");
                return 1L; // Default admin user ID
            }
        } catch (Exception e) {
            logger.warn("Error extracting user ID from security context: {}", e.getMessage());
            return 1L; // Default admin user ID
        }
    }
}