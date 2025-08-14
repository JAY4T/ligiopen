package com.jabulani.ligiopen.service.player;

import com.jabulani.ligiopen.entity.player.PlayerTransfer;
import com.jabulani.ligiopen.entity.player.ClubMembership;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing player transfers
 */
public interface TransferService {
    
    /**
     * Create a new transfer request
     * @param playerId Player ID
     * @param fromClubId Current club ID (can be null for free agents)
     * @param toClubId Destination club ID
     * @param transferType Transfer type
     * @param contractType Contract type
     * @param transferFee Transfer fee (optional)
     * @param proposedSalary Proposed salary
     * @param contractDuration Contract duration in months
     * @param notes Additional notes
     * @return Created PlayerTransfer entity
     */
    PlayerTransfer createTransferRequest(Long playerId, Long fromClubId, Long toClubId,
                                       PlayerTransfer.TransferType transferType,
                                       ClubMembership.ContractType contractType,
                                       BigDecimal transferFee, BigDecimal proposedSalary,
                                       Integer contractDuration, String notes);
    
    /**
     * Get transfer by ID
     * @param transferId Transfer ID
     * @return Transfer entity if found
     */
    Optional<PlayerTransfer> getTransferById(Long transferId);
    
    /**
     * Approve a transfer request
     * @param transferId Transfer ID
     * @param approvedBy User ID who approved
     * @return Updated transfer entity
     */
    PlayerTransfer approveTransfer(Long transferId, Long approvedBy);
    
    /**
     * Reject a transfer request
     * @param transferId Transfer ID
     * @param rejectedBy User ID who rejected
     * @param rejectionReason Reason for rejection
     * @return Updated transfer entity
     */
    PlayerTransfer rejectTransfer(Long transferId, Long rejectedBy, String rejectionReason);
    
    /**
     * Complete a transfer (both clubs have agreed)
     * @param transferId Transfer ID
     * @return Updated transfer entity
     */
    PlayerTransfer completeTransfer(Long transferId);
    
    /**
     * Cancel a transfer request
     * @param transferId Transfer ID
     * @param cancelledBy User ID who cancelled
     * @param cancellationReason Reason for cancellation
     * @return Updated transfer entity
     */
    PlayerTransfer cancelTransfer(Long transferId, Long cancelledBy, String cancellationReason);
    
    /**
     * Get all transfers for a player
     * @param playerId Player ID
     * @return List of transfers
     */
    List<PlayerTransfer> getPlayerTransfers(Long playerId);
    
    /**
     * Get pending transfers for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of pending transfers
     */
    List<PlayerTransfer> getPendingTransfersForClub(Long clubId, PageRequest pageRequest);
    
    /**
     * Get incoming transfers for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of incoming transfers
     */
    List<PlayerTransfer> getIncomingTransfers(Long clubId, PageRequest pageRequest);
    
    /**
     * Get outgoing transfers for a club
     * @param clubId Club ID
     * @param pageRequest Pagination information
     * @return List of outgoing transfers
     */
    List<PlayerTransfer> getOutgoingTransfers(Long clubId, PageRequest pageRequest);
    
    /**
     * Get recent transfers
     * @param limit Number of recent transfers to return
     * @return List of recent transfers
     */
    List<PlayerTransfer> getRecentTransfers(int limit);
    
    /**
     * Get transfers by date range
     * @param startDate Start date
     * @param endDate End date
     * @param pageRequest Pagination information
     * @return List of transfers in date range
     */
    List<PlayerTransfer> getTransfersByDateRange(LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
    
    /**
     * Get high-value transfers
     * @param minimumValue Minimum transfer value
     * @param limit Number of transfers to return
     * @return List of high-value transfers
     */
    List<PlayerTransfer> getHighValueTransfers(BigDecimal minimumValue, int limit);
    
    /**
     * Check if player has pending transfer
     * @param playerId Player ID
     * @return True if player has pending transfer
     */
    boolean hasPlayerPendingTransfer(Long playerId);
    
    /**
     * Get transfer statistics for a club
     * @param clubId Club ID
     * @return Transfer statistics map
     */
    java.util.Map<String, Object> getClubTransferStatistics(Long clubId);
}