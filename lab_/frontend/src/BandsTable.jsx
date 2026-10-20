import { useEffect, useRef, useState } from 'react'
import BandForm from './BandForm'
import { api } from './api'
import BandView from './BandView'

const COLUMNS = [
  { title: 'ID',          sort: 'id',                  value: b => b.id },
  { title: 'название',    sort: 'name',                filter: 'name',        value: b => b.name },
  { title: 'X',           sort: 'coordinates.x',       value: b => b.coordinates.x },
  { title: 'Y',           sort: 'coordinates.y',       value: b => b.coordinates.y },
  { title: 'создана',     sort: 'creationDate',        value: b => b.creationDate },
  { title: 'жанр',        sort: 'genre',               filter: 'genre',       value: b => b.genre },
  { title: 'участники',  sort: 'numberOfParticipants', value: b => b.numberOfParticipants ?? '' },
  { title: 'синглы',     sort: 'singlesCount',        value: b => b.singlesCount },
  { title: 'описание',    sort: 'description',         filter: 'description', value: b => b.description },
  { title: 'лучший альбом', sort: 'bestAlbum.name',    filter: 'album',       value: b => b.bestAlbum?.name ?? '' },
  { title: 'альбомы',    sort: 'albumsCount',         value: b => b.albumsCount },
  { title: 'фронтмен',    sort: 'frontMan.name',       filter: 'frontMan',    value: b => b.frontMan.name },
]

export default function BandsTable() {
  const [page, setPage] = useState(0)
  const [sort, setSort] = useState({ field: 'id', dir: 'asc' })
  const [filters, setFilters] = useState({})
  const [data, setData] = useState(null)
  const [editing, setEditing] = useState(undefined)
  const [viewing, setViewing] = useState(null)
  const [searchId, setSearchId] = useState('')

  function load() {
    const params = new URLSearchParams({
      page,
      size: 10,
      sort: `${sort.field},${sort.dir}`,
    })
    for (const [key, value] of Object.entries(filters)) {
      if (value) params.set(key, value)
    }
    fetch(`/api/bands?${params}`)
      .then(r => r.json())
      .then(setData)
  }

  useEffect(load, [page, sort, filters])

  const loadRef = useRef(load)
  loadRef.current = load
  useEffect(() => {
    const proto = location.protocol === 'https:' ? 'wss' : 'ws'
    const ws = new WebSocket(`${proto}://${location.host}/ws`)
    ws.onmessage = () => loadRef.current()
    return () => ws.close()
  }, [])

  function clickHeader(field) {
    setSort(s =>
      s.field === field
        ? { field, dir: s.dir === 'asc' ? 'desc' : 'asc' }
        : { field, dir: 'asc' })
    setPage(0)
  }

  async function removeBand(b) {
    if (!confirm(`удалить группу «${b.name}»?`)) return
      try {
        await api(`/api/bands/${b.id}`, { method: 'DELETE' })
        load()
      } catch (err) {
        alert(err.error ?? 'не удалось удалить')
    }
  }

  async function openById() {
      try {
        setViewing(await api(`/api/bands/${searchId}`))
      } catch (err) {
        alert(err.error ?? 'не удалось найти группу')
      }
  }

  function changeFilter(key, value) {
    setFilters(f => ({ ...f, [key]: value }))
    setPage(0)
  }

  if (!data) return <p>загрузка ♡ ～('▽^人) </p>

  return (
    <div>
        <button onClick={() => setEditing(null)}>создать группу</button>
        <input type="number" placeholder="ID группы" value={searchId}
               onChange={e => setSearchId(e.target.value)} />{' '}
        <button disabled={!searchId} onClick={openById}>найти по ID</button>
      <h1>музыкальные группы ( ´ ∀ `)ノ～ ♡ </h1>
      <table border="1" cellPadding="4">
        <thead>
          <tr>
            {COLUMNS.map(c => (
              <th
                key={c.sort}
                onClick={() => clickHeader(c.sort)}
                style={{ cursor: 'pointer' }}
              >
                {c.title}
                {sort.field === c.sort && (
                  sort.dir === 'asc' ? ' ▲' : ' ▼'
                )}
              </th>
            ))}
            <th>действия</th>
          </tr>

          <tr>
            {COLUMNS.map(c => (
              <th key={c.sort}>
                {c.filter && (
                  <input
                    size="8"
                    placeholder="фильтр"
                    value={filters[c.filter] ?? ''}
                    onChange={e => changeFilter(c.filter, e.target.value)}
                  />
                )}
              </th>
            ))}
            <th></th>
          </tr>
        </thead>
        <tbody>
          {data.content.map(b => (
            <tr key={b.id}>
              {COLUMNS.map(c => <td key={c.sort}>{c.value(b)}</td>)}
              <td>
                <button onClick={() => setViewing(b)}>просмотр</button>{' '}
                <button onClick={() => setEditing(b)}>изменить</button>{' '}
                <button onClick={() => removeBand(b)}>удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button disabled={page === 0} onClick={() => setPage(page - 1)}>назад</button>
      <span> стр. {page + 1} из {Math.max(data.totalPages, 1)} (всего: {data.totalElements}) </span>
      <button disabled={page + 1 >= data.totalPages} onClick={() => setPage(page + 1)}>вперед</button>
      {editing !== undefined && (
        <BandForm
          key={editing?.id ?? 'new'}
          band={editing}
          onClose={() => setEditing(undefined)}
          onSaved={() => { setEditing(undefined); load() }}
        />
      )}
      {viewing && (
        <BandView
          band={viewing}
          onClose={() => setViewing(null)}
          onEdit={b => { setViewing(null); setEditing(b) }}
        />
      )}
    </div>
  )
}