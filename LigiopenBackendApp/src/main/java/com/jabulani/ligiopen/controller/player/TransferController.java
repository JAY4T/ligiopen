package com.jabulani.ligiopen.controller.player;

import com.jabulani.ligiopen.dto.player.TransferRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * REST Controller interface for managing player transfers
 */
@Tag(name = "Transfer Management", description = "APIs for managing player transfers")
@RequestMapping("/api/transfers")
public interface TransferController {
    
    /**
     * Create a new transfer request
     */
    @Operation(summary = "Create transfer request", description = "Create a new player transfer request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transfer request created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request data"),
        @ApiResponse(responseCode = "404", description = "Player or club not found"),
        @ApiResponse(responseCode = "409", description = "Player already has pending transfer")
    })
    @PostMapping
    ResponseEntity<Object> createTransferRequest(@Valid @RequestBody TransferRequestDto transferRequestDto);
    
    /**
     * Get transfer by ID
     */
    @Operation(summary = "Get transfer by ID", description = "Retrieve transfer details by transfer ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer found"),
        @ApiResponse(responseCode = "404", description = "Transfer not found")
    })
    @GetMapping("/{transferId}")
    ResponseEntity<Object> getTransferById(@Parameter(description = "Transfer ID") @PathVariable Long transferId);
    
    /**
     * Approve transfer request
     */
    @Operation(summary = "Approve transfer", description = "Approve a pending transfer request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer approved successfully"),
        @ApiResponse(responseCode = "404", description = "Transfer not found"),
        @ApiResponse(responseCode = "400", description = "Transfer cannot be approved in current status")
    })
    @PutMapping("/{transferId}/approve")
    ResponseEntity<Object> approveTransfer(@Parameter(description = "Transfer ID") @PathVariable Long transferId);
    
    /**
     * Reject transfer request
     */
    @Operation(summary = "Reject transfer", description = "Reject a pending transfer request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Transfer not found"),
        @ApiResponse(responseCode = "400", description = "Transfer cannot be rejected in current status")
    })
    @PutMapping("/{transferId}/reject")
    ResponseEntity<Object> rejectTransfer(@Parameter(description = "Transfer ID") @PathVariable Long transferId,
                                        @Parameter(description = "Rejection reason") @RequestParam String reason);
    
    /**
     * Complete transfer
     */
    @Operation(summary = "Complete transfer", description = "Complete an approved transfer request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
        @ApiResponse(responseCode = "404", description = "Transfer not found"),
        @ApiResponse(responseCode = "400", description = "Transfer cannot be completed in current status")
    })
    @PutMapping("/{transferId}/complete")
    ResponseEntity<Object> completeTransfer(@Parameter(description = "Transfer ID") @PathVariable Long transferId);
    
    /**
     * Cancel transfer request
     */
    @Operation(summary = "Cancel transfer", description = "Cancel a transfer request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Transfer not found"),
        @ApiResponse(responseCode = "400", description = "Transfer cannot be cancelled in current status")
    })
    @PutMapping("/{transferId}/cancel")
    ResponseEntity<Object> cancelTransfer(@Parameter(description = "Transfer ID") @PathVariable Long transferId,
                                        @Parameter(description = "Cancellation reason") @RequestParam String reason);
    
    /**
     * Get pending transfers for a club
     */
    @Operation(summary = "Get pending transfers for club", description = "Get all pending transfer requests for a specific club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending transfers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/club/{clubId}/pending")
    ResponseEntity<Object> getPendingTransfersForClub(@Parameter(description = "Club ID") @PathVariable Long clubId,
                                                     @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
                                                     @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size);
    
    /**
     * Get incoming transfers for a club
     */
    @Operation(summary = "Get incoming transfers", description = "Get incoming transfer requests for a club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incoming transfers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/club/{clubId}/incoming")
    ResponseEntity<Object> getIncomingTransfers(@Parameter(description = "Club ID") @PathVariable Long clubId,
                                              @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
                                              @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size);
    
    /**
     * Get outgoing transfers for a club
     */
    @Operation(summary = "Get outgoing transfers", description = "Get outgoing transfer requests for a club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Outgoing transfers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/club/{clubId}/outgoing")
    ResponseEntity<Object> getOutgoingTransfers(@Parameter(description = "Club ID") @PathVariable Long clubId,
                                              @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
                                              @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size);
    
    /**
     * Get recent transfers
     */
    @Operation(summary = "Get recent transfers", description = "Get recently completed transfers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recent transfers retrieved successfully")
    })
    @GetMapping("/recent")
    ResponseEntity<Object> getRecentTransfers(@Parameter(description = "Number of transfers to return") @RequestParam(defaultValue = "10") int limit);
    
    /**
     * Get transfers by date range
     */
    @Operation(summary = "Get transfers by date range", description = "Get transfers within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfers retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range")
    })
    @GetMapping("/date-range")
    ResponseEntity<Object> getTransfersByDateRange(@Parameter(description = "Start date") @RequestParam LocalDate startDate,
                                                  @Parameter(description = "End date") @RequestParam LocalDate endDate,
                                                  @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
                                                  @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size);
    
    /**
     * Get high-value transfers
     */
    @Operation(summary = "Get high-value transfers", description = "Get transfers above a minimum value")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "High-value transfers retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid minimum value")
    })
    @GetMapping("/high-value")
    ResponseEntity<Object> getHighValueTransfers(@Parameter(description = "Minimum transfer value") @RequestParam BigDecimal minimumValue,
                                               @Parameter(description = "Number of transfers to return") @RequestParam(defaultValue = "10") int limit);
    
    /**
     * Get club transfer statistics
     */
    @Operation(summary = "Get club transfer statistics", description = "Get transfer statistics for a specific club")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer statistics retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Club not found")
    })
    @GetMapping("/club/{clubId}/statistics")
    ResponseEntity<Object> getClubTransferStatistics(@Parameter(description = "Club ID") @PathVariable Long clubId);
}