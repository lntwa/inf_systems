import { useEffect, useState } from 'react'
import { api } from './api'

const GENRES = ['PSYCHEDELIC_CLOUD_RAP', 'SOUL', 'BLUES', 'BRIT_POP']
const toNum = v => (v === '' ? null : Number(v))

function Field({ label, error, children }) {
  return (
    <div style={{ marginBottom: 8 }}>
      <label>{label}<br />{children}</label>
      {error && <div style={{ color: 'red' }}>{error}</div>}
    </div>
  )
}

export default function BandForm({ band, onClose, onSaved }) {
  const [form, setForm] = useState({
    name: band?.name ?? '',
    coordinatesId: band?.coordinates.id ?? '',
    genre: band?.genre ?? '',
    numberOfParticipants: band?.numberOfParticipants ?? '',
    singlesCount: band?.singlesCount ?? '',
    description: band?.description ?? '',
    bestAlbumId: band?.bestAlbum?.id ?? '',
    albumsCount: band?.albumsCount ?? '',
    frontManId: band?.frontMan.id ?? '',
  })
  const [errors, setErrors] = useState({})
  const [options, setOptions] = useState({ coordinates: [], persons: [], albums: [] })

  useEffect(() => {
    Promise.all([api('/api/coordinates'), api('/api/persons'), api('/api/albums')])
      .then(([coordinates, persons, albums]) => setOptions({ coordinates, persons, albums }))
  }, [])

  const set = (key, value) => setForm(f => ({ ...f, [key]: value }))

  async function submit(e) {
    e.preventDefault()
    const body = {
      name: form.name,
      coordinatesId: toNum(form.coordinatesId),
      genre: form.genre || null,
      numberOfParticipants: toNum(form.numberOfParticipants),
      singlesCount: toNum(form.singlesCount),
      description: form.description,
      bestAlbumId: toNum(form.bestAlbumId),
      albumsCount: toNum(form.albumsCount),
      frontManId: toNum(form.frontManId),
    }
    try {
      await api(band ? `/api/bands/${band.id}` : '/api/bands', {
        method: band ? 'PUT' : 'POST',
        body: JSON.stringify(body),
      })
      onSaved()
    } catch (err) {
      setErrors(err)
    }
  }

  return (
    <div className="overlay">
      <form onSubmit={submit} className="modal">
        <h2>{band ? `изменить группу #${band.id}` : 'новая группа'}</h2>
        {errors.error && <div style={{ color: 'red', marginBottom: 8 }}>{errors.error}</div>}

        <Field label="название" error={errors.name}>
          <input value={form.name} onChange={e => set('name', e.target.value)} />
        </Field>

        <Field label="координаты" error={errors.coordinatesId}>
          <select value={form.coordinatesId} onChange={e => set('coordinatesId', e.target.value)}>
            <option value="">— выберите —</option>
            {options.coordinates.map(c =>
              <option key={c.id} value={c.id}>#{c.id} ({c.x}; {c.y})</option>)}
          </select>
        </Field>

        <Field label="жанр" error={errors.genre}>
          <select value={form.genre} onChange={e => set('genre', e.target.value)}>
            <option value="">— выберите —</option>
            {GENRES.map(g => <option key={g} value={g}>{g}</option>)}
          </select>
        </Field>

        <Field label="количество участников" error={errors.numberOfParticipants}>
          <input type="number" min="1" value={form.numberOfParticipants}
                 onChange={e => set('numberOfParticipants', e.target.value)} />
        </Field>

        <Field label="количество синглов" error={errors.singlesCount}>
          <input type="number" min="1" value={form.singlesCount}
                 onChange={e => set('singlesCount', e.target.value)} />
        </Field>

        <Field label="описание" error={errors.description}>
          <input value={form.description} onChange={e => set('description', e.target.value)} />
        </Field>

        <Field label="лучший альбом" error={errors.bestAlbumId}>
          <select value={form.bestAlbumId} onChange={e => set('bestAlbumId', e.target.value)}>
            <option value="">— нет —</option>
            {options.albums.map(a =>
              <option key={a.id} value={a.id}>#{a.id} {a.name}</option>)}
          </select>
        </Field>

        <Field label="количество альбомов" error={errors.albumsCount}>
          <input type="number" min="1" value={form.albumsCount}
                 onChange={e => set('albumsCount', e.target.value)} />
        </Field>

        <Field label="фронтмен" error={errors.frontManId}>
          <select value={form.frontManId} onChange={e => set('frontManId', e.target.value)}>
            <option value="">— выберите —</option>
            {options.persons.map(p =>
              <option key={p.id} value={p.id}>#{p.id} {p.name}</option>)}
          </select>
        </Field>

        <button type="submit">сохранить</button>{' '}
        <button type="button" onClick={onClose}>отмена</button>
      </form>
    </div>
  )
}