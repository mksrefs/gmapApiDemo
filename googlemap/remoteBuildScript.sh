#!/bin/sh

LOCALPROJECTDIR="このプロジェクトのパス"
LOCALPRODUCT=${LOCALPROJECTDIR}/target/universal/googlemap-1.0-SNAPSHOT.zip
USERNAME="linux user account name"
REMOTEDIR="リモートサーバに成果物を配置するパス"
TARGETIPADDRESS="リモートサーバのIP"

echo "リモート先の転送ファイル確認"
ssh ${USERNAME}@${TARGETIPADDRESS} ls ${REMOTEDIR} >& /dev/null
if [ $? -eq 0 ]; then
echo "リモート先の転送ファイル削除"
ssh -t  ${USERNAME}@${TARGETIPADDRESS} rm -rf ${REMOTEDIR}/googlemap-1.0-SNAPSHOT
fi
echo "成果物生成"
cd ${LOCALPROJECTDIR}
activator dist
if [ $? -eq 1 ]; then
echo "activator distエラー"
exit 1
fi
echo "成果物に実行権限付与"
chmod +x ${LOCALPRODUCT}
echo "scpを開始します"
scp -rp ${LOCALPRODUCT} ${USERNAME}@${TARGETIPADDRESS}:${REMOTEDIR}
if [ $? -eq 1 ]; then
echo "scpエラー"
exit 1
fi
echo "buildスクリプトscp開始"
scp -rp ${LOCALPROJECTDIR}buildProduct.sh ${USERNAME}@${TARGETIPADDRESS}:${REMOTEDIR}
if [ $? -eq 1 ]; then
echo "scpエラー"
exit 1
fi
echo "scp完了"
echo "転送先のbuild.shを実行"
ssh ${USERNAME}@${TARGETIPADDRESS} ${REMOTEDIR}buildProduct.sh
