package org.study.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.ProcedureInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.MasterObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.master.RegionPlan;
import org.apache.hadoop.hbase.master.procedure.MasterProcedureEnv;
import org.apache.hadoop.hbase.procedure2.ProcedureExecutor;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.Quotas;

/**
 * 
 * @Title: LocalMasterObserver
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月12日 下午3:08:43
 */
public class LocalMasterObserver implements MasterObserver {

	@Override
	public void start(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preCreateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc,
			HRegionInfo[] regions) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postCreateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc,
			HRegionInfo[] regions) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preCreateTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc,
			HRegionInfo[] regions) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postCreateTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, HTableDescriptor desc,
			HRegionInfo[] regions) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preTruncateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postTruncateTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preTruncateTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postTruncateTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preModifyTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HTableDescriptor htd) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postModifyTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HTableDescriptor htd) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preModifyTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HTableDescriptor htd) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postModifyTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HTableDescriptor htd) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preAddColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor column) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postAddColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor column) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preAddColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor column) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postAddColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor column) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preModifyColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor descriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postModifyColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor descriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preModifyColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor descriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postModifyColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			HColumnDescriptor descriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName, byte[] c)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteColumn(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName, byte[] c)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName, byte[] c)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteColumnHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName,
			byte[] c) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preEnableTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postEnableTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preEnableTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postEnableTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDisableTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDisableTable(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDisableTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDisableTableHandler(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preMove(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo region, ServerName srcServer,
			ServerName destServer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postMove(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo region, ServerName srcServer,
			ServerName destServer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preAbortProcedure(ObserverContext<MasterCoprocessorEnvironment> ctx,
			ProcedureExecutor<MasterProcedureEnv> procEnv, long procId) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postAbortProcedure(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preListProcedures(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postListProcedures(ObserverContext<MasterCoprocessorEnvironment> ctx, List<ProcedureInfo> procInfoList)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preAssign(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postAssign(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preUnassign(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo, boolean force)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postUnassign(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo, boolean force)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preRegionOffline(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postRegionOffline(ObserverContext<MasterCoprocessorEnvironment> ctx, HRegionInfo regionInfo)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preBalance(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postBalance(ObserverContext<MasterCoprocessorEnvironment> ctx, List<RegionPlan> plans)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preBalanceSwitch(ObserverContext<MasterCoprocessorEnvironment> ctx, boolean newValue)
			throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postBalanceSwitch(ObserverContext<MasterCoprocessorEnvironment> ctx, boolean oldValue, boolean newValue)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preShutdown(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preStopMaster(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postStartMaster(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preMasterInitialization(ObserverContext<MasterCoprocessorEnvironment> ctx) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preListSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postListSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preCloneSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postCloneSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preRestoreSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postRestoreSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot,
			HTableDescriptor hTableDescriptor) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteSnapshot(ObserverContext<MasterCoprocessorEnvironment> ctx, SnapshotDescription snapshot)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preGetTableDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<TableName> tableNamesList, List<HTableDescriptor> descriptors) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGetTableDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<HTableDescriptor> descriptors) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preGetTableDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<TableName> tableNamesList, List<HTableDescriptor> descriptors, String regex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGetTableDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<TableName> tableNamesList, List<HTableDescriptor> descriptors, String regex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preGetTableNames(ObserverContext<MasterCoprocessorEnvironment> ctx, List<HTableDescriptor> descriptors,
			String regex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGetTableNames(ObserverContext<MasterCoprocessorEnvironment> ctx, List<HTableDescriptor> descriptors,
			String regex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preCreateNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, NamespaceDescriptor ns)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postCreateNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, NamespaceDescriptor ns)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDeleteNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, String namespace)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postDeleteNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, String namespace)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preModifyNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, NamespaceDescriptor ns)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postModifyNamespace(ObserverContext<MasterCoprocessorEnvironment> ctx, NamespaceDescriptor ns)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preGetNamespaceDescriptor(ObserverContext<MasterCoprocessorEnvironment> ctx, String namespace)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGetNamespaceDescriptor(ObserverContext<MasterCoprocessorEnvironment> ctx, NamespaceDescriptor ns)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preListNamespaceDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<NamespaceDescriptor> descriptors) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postListNamespaceDescriptors(ObserverContext<MasterCoprocessorEnvironment> ctx,
			List<NamespaceDescriptor> descriptors) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preTableFlush(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postTableFlush(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName, Quotas quotas)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName, Quotas quotas)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName, TableName tableName,
			Quotas quotas) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName,
			TableName tableName, Quotas quotas) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName, String namespace,
			Quotas quotas) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSetUserQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String userName, String namespace,
			Quotas quotas) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSetTableQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName, Quotas quotas)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSetTableQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, TableName tableName, Quotas quotas)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSetNamespaceQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String namespace, Quotas quotas)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSetNamespaceQuota(ObserverContext<MasterCoprocessorEnvironment> ctx, String namespace,
			Quotas quotas) throws IOException {
		// TODO Auto-generated method stub

	}

}
