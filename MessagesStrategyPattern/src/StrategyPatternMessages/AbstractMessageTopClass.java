package StrategyPatternMessages;

import java.math.BigInteger;
import java.util.UUID;

import se.his.drts.message.MessagePayload;


public class AbstractMessageTopClass extends MessagePayload {
	private static final long serialVersionUID = 1L;

	public static class MessageIdentity implements Comparable<MessageIdentity> {
		private UUID uuid;
		private BigInteger subIdentity;

		public MessageIdentity(UUID uuid2, BigInteger subIdentity2) {
			this.uuid = uuid2;
			this.subIdentity = subIdentity2;
		}

		@Override
		public int compareTo(MessageIdentity arg0) {
			final int n = this.uuid.compareTo(arg0.uuid);
			if (n != 0) {
				return n;
			}
			return this.subIdentity.compareTo(arg0.subIdentity);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			final MessageIdentity mi = (MessageIdentity) obj;
			return this.uuid.equals(mi.uuid) && this.subIdentity.equals(mi.subIdentity);
		}

		/**
		 * @return the identity
		 */
		public final BigInteger getSubIdentity() {
			return subIdentity;
		}

		/**
		 * @return the uuid
		 */
		public final UUID getUuid() {
			return uuid;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.uuid.hashCode() + this.subIdentity.hashCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MessageIdentity [uuid=" + uuid + ", subIdentity=" + subIdentity + "]";
		}

	}
	private int attempt = 0;

	private static BigInteger nextSubIdentity = BigInteger.ONE;
	private static UUID uuid = UUID.fromString("32eb76f7-e72b-4fa5-ad02-95d92115c452");
	private BigInteger subIdentity;

	protected AbstractMessageTopClass() {
		super(AbstractMessageTopClass.uuid);
		synchronized (AbstractMessageTopClass.nextSubIdentity) {
			this.subIdentity = AbstractMessageTopClass.nextSubIdentity;
			AbstractMessageTopClass.nextSubIdentity = AbstractMessageTopClass.nextSubIdentity.add(BigInteger.ONE);
		}
	}

	protected AbstractMessageTopClass(UUID uuid) {
		super(uuid);
		synchronized (AbstractMessageTopClass.nextSubIdentity) {
			this.subIdentity = AbstractMessageTopClass.nextSubIdentity;
			AbstractMessageTopClass.nextSubIdentity = AbstractMessageTopClass.nextSubIdentity.add(BigInteger.ONE);
		}
	}

//	@Override
//	public void executeInClient() {
//
//	}
//
//	public void executeInFrontEndFromRM() {
//
//	}
//
//	public void executeInFrontEndFromClient() {
//
//	}
//
//	public void executeInReplicaManager() {
//
//	}

}
