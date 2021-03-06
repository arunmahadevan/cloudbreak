{% set configure_remote_db = salt['pillar.get']('postgres:configure_remote_db', 'None') %}

{% if 'None' != configure_remote_db %}

include:
  - postgresql.disaster_recovery

restore_postgresql_db:
  cmd.run:
    - name: /opt/salt/scripts/restore_db.sh {{salt['pillar.get']('platform')}} {{salt['pillar.get']('disaster_recovery:object_storage_url')}} {{salt['pillar.get']('postgres:remote_db_url')}} {{salt['pillar.get']('postgres:remote_db_port')}} {{salt['pillar.get']('postgres:remote_admin')}}
    - require:
        - sls: postgresql.disaster_recovery

{%- else %}
{# Intentionally left blank, we're not handling backup of non-remote (embedded) DBs #}
{% endif %}
